package ru.yandex.yandexlavka.services.courierservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.yandex.yandexlavka.entity.Courier;
import ru.yandex.yandexlavka.entity.Order;
import ru.yandex.yandexlavka.entity.OrderGroup;
import ru.yandex.yandexlavka.entity.dto.CourierDto;
import ru.yandex.yandexlavka.entity.dto.CourierMetaInfoDto;
import ru.yandex.yandexlavka.entity.dto.assignments.CouriersAssignDto;
import ru.yandex.yandexlavka.entity.dto.assignments.OrderGroupDto;
import ru.yandex.yandexlavka.entity.dto.assignments.OrdersDto;
import ru.yandex.yandexlavka.entity.enums.CourierType;
import ru.yandex.yandexlavka.repositories.CourierRepository;
import ru.yandex.yandexlavka.repositories.OrderRepository;
import ru.yandex.yandexlavka.utils.Constants;
import ru.yandex.yandexlavka.utils.mapping.CourierMapping;
import ru.yandex.yandexlavka.utils.mapping.OrderMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.List;

@Service
public class CourierServiceImpl implements CourierService{
    @Autowired
    private CourierRepository courierRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CourierMapping courierMapping;
    @Autowired
    private OrderMapping orderMapping;

    @Override
    public List<CourierDto> getAllCouriers(int offset, int limit) {
        return courierRepository.findAll(PageRequest.of(offset, limit))
                .stream().map(courierMapping::mapToCourierDto)
                .toList();
    }

    @Override
    public void addCourier(CourierDto dto) {
        Courier courier = courierMapping.mapToCourierEntity(dto);
        setParametersForCourier(courier);
        courierRepository.save(courier);
        dto.setCourierId(courier.getId());
    }

    @Override
    public CourierDto getCourierById(Long id) {
        return courierMapping.mapToCourierDto(
                courierRepository.findById(id)
                        .orElse(new Courier()));
    }

    @Override
    public CouriersAssignDto getCouriersWithOrders(LocalDate date, Long courierId) {
        CouriersAssignDto response = new CouriersAssignDto(date.toString());
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

    private void getOrdersAssignCourier(Courier courier, CouriersAssignDto response) {
        OrdersDto couriersDto = new OrdersDto(courier.getId());
        for (OrderGroup group : courier.getOrderGroups()) {
            OrderGroupDto orderGroupDto = new OrderGroupDto(group.getId());
            for (Order order : group.getOrders()) {
                orderGroupDto.getOrders().add(orderMapping.mapToOrderDto(order));
            }
            couriersDto.getOrderGroupDto().add(orderGroupDto);
        }
        response.getCouriers().add(couriersDto);
    }

    @Override
    public CourierMetaInfoDto getRatingAndEarning(Long courierId, LocalDate startDate, LocalDate endDate) {
        Courier courier = courierRepository.findById(courierId).orElseThrow();

        if (courier.getOrderGroups().isEmpty()) {
            throw new NullPointerException();
        }

        CourierDto dto = courierMapping.mapToCourierDto(courier);
        CourierMetaInfoDto courierMetaInfoDto = new CourierMetaInfoDto(
                dto.getCourierId(),
                dto.getCourierType(),
                dto.getRegions(),
                dto.getWorkingHours());
        courierMetaInfoDto.setEarnings(getEarningByCourier(courier));
        courierMetaInfoDto.setRating(getRatingByCourier(courier, startDate, endDate));
        return courierMetaInfoDto;
    }

    private Integer getRatingByCourier(Courier courier, LocalDate startDate, LocalDate endDate) {
        Period period = Period.between(startDate, endDate);
        int hours = (period.getYears() * 8760) + (period.getMonths() * 672) + (period.getDays() * 24);
        int countOrder = 0;
        for (OrderGroup orderGroup : courier.getOrderGroups()) {
            countOrder += orderGroup.getOrders().size();
        }
        return (countOrder / hours) * courier.getRatingRate();
    }

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
