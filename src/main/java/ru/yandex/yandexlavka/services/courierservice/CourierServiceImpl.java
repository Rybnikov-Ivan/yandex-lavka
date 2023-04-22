package ru.yandex.yandexlavka.services.courierservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.yandex.yandexlavka.entity.Courier;
import ru.yandex.yandexlavka.entity.Order;
import ru.yandex.yandexlavka.entity.dto.CourierAssignDto;
import ru.yandex.yandexlavka.entity.dto.CourierDto;
import ru.yandex.yandexlavka.entity.dto.OrderDto;
import ru.yandex.yandexlavka.entity.enums.CourierType;
import ru.yandex.yandexlavka.repositories.CourierRepository;
import ru.yandex.yandexlavka.utils.Constants;
import ru.yandex.yandexlavka.utils.mapping.CourierMapping;
import ru.yandex.yandexlavka.utils.mapping.OrderMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourierServiceImpl implements CourierService{
    @Autowired
    private CourierRepository courierRepository;
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
        dto.setCourier_id(courier.getId());
    }

    @Override
    public CourierDto getCourierById(Long id) {
        return courierMapping.mapToCourierDto(
                courierRepository.findById(id)
                        .orElse(new Courier()));
    }

    @Override
    public List<CourierAssignDto> getCouriersWithOrders(LocalDate date, Long courierId) {
        List<CourierAssignDto> courierAssignDtoList = new ArrayList<>();
        if (courierId == 0) {
            List<Courier> couriers = courierRepository.findAll();
            for (Courier courier : couriers) {
                if (courier.getOrders().size() > 0) {
                    getOrdersAssignCourier(courier, courierAssignDtoList);
                }
            }
        } else {
            Courier courier = courierRepository.findById(courierId).orElseThrow();
            if (courier.getOrders() != null) {
                getOrdersAssignCourier(courier, courierAssignDtoList);
            }
        }
        return courierAssignDtoList;
    }

    private void getOrdersAssignCourier(Courier courier, List<CourierAssignDto> courierAssignDtoList) {
        CourierAssignDto dto = new CourierAssignDto();
        dto.setCourier_id(courier.getId());
        List<OrderDto> orderDtoList = new ArrayList<>();
        for (Order order : courier.getOrders()) {
            orderDtoList.add(orderMapping.mapToOrderDto(order));
        }
        dto.setOrders(orderDtoList);
        courierAssignDtoList.add(dto);
    }
    private void setParametersForCourier(Courier courier) {
        if (courier.getCourierType().equals(CourierType.AUTO)) {
            courier.setMaxWeight(Constants.LIMIT_WEIGHT_AUTO);
            courier.setMaxCount(Constants.LIMIT_COUNT_AUTO);
            courier.setFirstOrderTime(Constants.FIRST_ORDER_TIME_AUTO);
            courier.setNextOrderTime(Constants.NEXT_ORDER_TIME_AUTO);
        } else if (courier.getCourierType().equals(CourierType.BIKE)) {
            courier.setMaxWeight(Constants.LIMIT_WEIGHT_BIKE);
            courier.setMaxCount(Constants.LIMIT_COUNT_BIKE);
            courier.setFirstOrderTime(Constants.FIRST_ORDER_TIME_BIKE);
            courier.setNextOrderTime(Constants.NEXT_ORDER_TIME_BIKE);
        } else if (courier.getCourierType().equals(CourierType.FOOT)) {
            courier.setMaxWeight(Constants.LIMIT_WEIGHT_FOOT);
            courier.setMaxCount(Constants.LIMIT_COUNT_FOOT);
            courier.setFirstOrderTime(Constants.FIRST_ORDER_TIME_FOOT);
            courier.setNextOrderTime(Constants.NEXT_ORDER_TIME_FOOT);
        }
    }
}
