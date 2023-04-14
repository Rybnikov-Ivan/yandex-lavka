package ru.yandex.yandexlavka.utils.mapping;

import org.springframework.stereotype.Service;
import ru.yandex.yandexlavka.entity.IntervalTime;
import ru.yandex.yandexlavka.entity.Order;
import ru.yandex.yandexlavka.entity.dto.OrderDto;
import ru.yandex.yandexlavka.utils.Constants;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderMapping {
    public OrderDto mapToOrderDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setOrder_id(order.getId());
        dto.setWeight(order.getWeight());
        dto.setCost(order.getCost().intValue());
        dto.setRegions(order.getRegion());
        List<String> deliveryHours = new ArrayList<>();
        for (IntervalTime time : order.getDeliveryHours()) {
            deliveryHours.add(time.getStartTime() + "-" + time.getEndTime());
        }
        dto.setDelivery_hours(deliveryHours);
        return dto;
    }

    public Order mapToOrderEntity(OrderDto dto) {
        Order entity = new Order();
        entity.setId(dto.getOrder_id());
        entity.setWeight(dto.getWeight());
        entity.setCost(BigDecimal.valueOf(dto.getCost()));
        entity.setRegion(dto.getRegions());
        List<IntervalTime> deliveryHours = new ArrayList<>();
        for (String deliveryHour : dto.getDelivery_hours()) {
            LocalTime startTime = LocalTime.parse(deliveryHour.substring(Constants.BEGIN_INDEX_IN_FROM_DATE_STRING, Constants.END_INDEX_IN_FROM_DATE_STRING));
            LocalTime endTime = LocalTime.parse(deliveryHour.substring(Constants.BEGIN_INDEX_IN_TO_DATE_STRING, Constants.END_INDEX_IN_TO_DATE_STRING));
            IntervalTime time = new IntervalTime(startTime, endTime, entity);
            deliveryHours.add(time);
        }
        entity.setDeliveryHours(deliveryHours);
        return entity;
    }
}
