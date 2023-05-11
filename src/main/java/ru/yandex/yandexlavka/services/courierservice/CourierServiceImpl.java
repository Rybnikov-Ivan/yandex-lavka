package ru.yandex.yandexlavka.services.courierservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.yandex.yandexlavka.entity.Courier;
import ru.yandex.yandexlavka.entity.Order;
import ru.yandex.yandexlavka.entity.OrderGroup;
import ru.yandex.yandexlavka.entity.dto.CourierDto;
import ru.yandex.yandexlavka.entity.dto.OrderDto;
import ru.yandex.yandexlavka.entity.enums.CourierType;
import ru.yandex.yandexlavka.repositories.CourierRepository;
import ru.yandex.yandexlavka.utils.Constants;
import ru.yandex.yandexlavka.utils.mapping.CourierMapping;
import ru.yandex.yandexlavka.utils.mapping.OrderMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class CourierServiceImpl implements CourierService{
    @Autowired
    private CourierRepository courierRepository;
    @Autowired
    private CourierMapping courierMapping;
    @Autowired
    private OrderMapping orderMapping;

    /**
     * Получение всех курьеров
     * @param offset
     * @param limit
     * @return List {@link CourierDto.MainCourierDto}
     */
    @Override
    public List<CourierDto.MainCourierDto> getAllCouriers(int offset, int limit) {
        return courierRepository.findAll(PageRequest.of(offset, limit))
                .stream().map(courierMapping::mapToCourierDto)
                .toList();
    }

    /**
     * Добавление курьеров
     * @param dto
     * @return {@link CourierDto.MainCourierDto}
     */
    @Override
    public CourierDto.MainCourierDto addCourier(CourierDto.MainCourierDto dto) {
        Courier courier = courierMapping.mapToCourierEntity(dto);
        setParametersForCourier(courier);
        courierRepository.save(courier);
        return new CourierDto.MainCourierDto(
                courier.getId(),
                dto.courierType(),
                dto.regions(),
                dto.workingHours()
        );
    }

    /**
     * Получение курьера по Id
     * @param id
     * @return {@link CourierDto.MainCourierDto}
     */
    @Override
    public CourierDto.MainCourierDto getCourierById(Long id) {
        return courierMapping.mapToCourierDto(
                courierRepository.findById(id)
                        .orElse(new Courier()));
    }

    /**
     * Получение курьеров с заказами, определенными на него
     * @param date
     * @param courierId
     * @return {@link CourierDto.GetCouriersAssignOrdersResponse}
     */
    @Override
    public CourierDto.GetCouriersAssignOrdersResponse getCouriersWithOrders(LocalDate date, Long courierId) {
        CourierDto.GetCouriersAssignOrdersResponse response = new CourierDto.GetCouriersAssignOrdersResponse(date.toString(), new ArrayList<>());
        if (courierId == 0) {
            List<Courier> couriers = courierRepository.findAll();
            for (Courier courier : couriers) {
                if (courier.getOrderGroups().size() > 0) {
                    getOrdersAssignCourier(courier, response);
                }
            }
        } else {
            Courier courier = courierRepository.findById(courierId).orElseThrow();
            if (courier.getOrderGroups() != null) {
                getOrdersAssignCourier(courier, response);
            }
        }
        return response;
    }

    /**
     * Добавление тела в ответ сервиса getCouriersWithOrders
     * @param courier
     * @param response
     */
    private void getOrdersAssignCourier(Courier courier, CourierDto.GetCouriersAssignOrdersResponse response) {
        OrderDto.OrdersDto couriersDto = new OrderDto.OrdersDto(courier.getId(), new ArrayList<>());
        for (OrderGroup group : courier.getOrderGroups()) {
            OrderDto.OrderGroupDto orderGroupDto = new OrderDto.OrderGroupDto(group.getId(), new ArrayList<>());
            for (Order order : group.getOrders()) {
                orderGroupDto.orders().add(orderMapping.mapToOrderDto(order));
            }
            couriersDto.orderGroupDto().add(orderGroupDto);
        }
        response.couriers().add(couriersDto);
    }

    /**
     * Получение рейтинга и суммы заработанных денег
     * @param courierId
     * @param startDate
     * @param endDate
     * @return {@link CourierDto.GetCourierMetaInfoResponse}
     */
    @Override
    public CourierDto.GetCourierMetaInfoResponse getRatingAndEarning(Long courierId, LocalDate startDate, LocalDate endDate) {
        Courier courier = courierRepository.findById(courierId).orElseThrow();

        if (courier.getOrderGroups().isEmpty()) {
            throw new NullPointerException();
        }

        CourierDto.MainCourierDto dto = courierMapping.mapToCourierDto(courier);

        return new CourierDto.GetCourierMetaInfoResponse(
                dto.courierId(),
                dto.courierType(),
                dto.regions(),
                dto.workingHours(),
                getRatingByCourier(courier, startDate, endDate),
                getEarningByCourier(courier));
    }

    /**
     * Расчет рейтинга курьера
     * @param courier
     * @param startDate
     * @param endDate
     * @return Integer
     */
    private Integer getRatingByCourier(Courier courier, LocalDate startDate, LocalDate endDate) {
        Period period = Period.between(startDate, endDate);
        int hours = (period.getYears() * 8760) + (period.getMonths() * 672) + (period.getDays() * 24);
        int countOrder = 0;
        for (OrderGroup orderGroup : courier.getOrderGroups()) {
            countOrder += orderGroup.getOrders().size();
        }
        return (countOrder / hours) * courier.getRatingRate();
    }

    /**
     * Расчет заработка курьера
     * @param courier
     * @return Integer
     */
    private Integer getEarningByCourier(Courier courier) {
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal earningRate = BigDecimal.valueOf(courier.getEarningRate());
        for (OrderGroup orderGroup : courier.getOrderGroups()) {
            List<Order> orderSortByCost = orderGroup.getOrders().stream()
                    .sorted(Comparator.comparing(Order::getCost)).toList();
            for (int i = 0; i < orderSortByCost.size(); i++) {
                BigDecimal cost = orderSortByCost.get(i).getCost();
                if (i == 0) {
                    sum = sum.add(cost).multiply(earningRate);
                } else {
                    BigDecimal rate = BigDecimal.valueOf(Constants.NEXT_RATE_COST);
                    sum = sum.add(cost.multiply(earningRate).multiply(rate));
                }
            }
        }
        return sum.intValue();
    }

    /**
     * Установка свойств по допустимым параметрам
     * @param courier
     */
    private void setParametersForCourier(Courier courier) {
        if (courier.getCourierType().equals(CourierType.AUTO)) {
            courier.setMaxWeight(Constants.LIMIT_WEIGHT_AUTO);
            courier.setMaxCount(Constants.LIMIT_COUNT_AUTO);
            courier.setFirstOrderTime(Constants.FIRST_ORDER_TIME_AUTO);
            courier.setNextOrderTime(Constants.NEXT_ORDER_TIME_AUTO);
            courier.setEarningRate(Constants.EARNING_RATE_AUTO);
            courier.setRatingRate(Constants.RATING_RATE_AUTO);
        } else if (courier.getCourierType().equals(CourierType.BIKE)) {
            courier.setMaxWeight(Constants.LIMIT_WEIGHT_BIKE);
            courier.setMaxCount(Constants.LIMIT_COUNT_BIKE);
            courier.setFirstOrderTime(Constants.FIRST_ORDER_TIME_BIKE);
            courier.setNextOrderTime(Constants.NEXT_ORDER_TIME_BIKE);
            courier.setEarningRate(Constants.EARNING_RATE_BIKE);
            courier.setRatingRate(Constants.RATING_RATE_BIKE);
        } else if (courier.getCourierType().equals(CourierType.FOOT)) {
            courier.setMaxWeight(Constants.LIMIT_WEIGHT_FOOT);
            courier.setMaxCount(Constants.LIMIT_COUNT_FOOT);
            courier.setFirstOrderTime(Constants.FIRST_ORDER_TIME_FOOT);
            courier.setNextOrderTime(Constants.NEXT_ORDER_TIME_FOOT);
            courier.setEarningRate(Constants.EARNING_RATE_FOOT);
            courier.setRatingRate(Constants.RATING_RATE_FOOT);
        }
    }
}
