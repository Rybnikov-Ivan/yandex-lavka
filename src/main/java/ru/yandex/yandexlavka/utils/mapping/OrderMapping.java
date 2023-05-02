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
    public OrderDto.MainOrderDto mapToOrderDto(Order order) {
        List<String> deliveryHours = new ArrayList<>();
        for (IntervalTime time : order.getDeliveryHours()) {
            deliveryHours.add(time.getStartTime() + "-" + time.getEndTime());
        }
        String completedTime = order.getCompletedTime() != null ? String.valueOf(order.getCompletedTime()) : null;

        return new OrderDto.MainOrderDto(
                order.getId(),
                order.getWeight(),
                order.getRegion(),
                deliveryHours,
                order.getCost().intValue(),
                completedTime);
    }

    public Order mapToOrderEntity(OrderDto.MainOrderDto dto) {
        Order entity = new Order();
        entity.setId(dto.orderId());
        entity.setWeight(dto.weight());
        entity.setCost(BigDecimal.valueOf(dto.cost()));
        entity.setRegion(dto.region());
        List<IntervalTime> deliveryHours = new ArrayList<>();
        for (String deliveryHour : dto.deliveryHours()) {
            LocalTime startTime = LocalTime.parse(deliveryHour.substring(Constants.BEGIN_INDEX_IN_FROM_DATE_STRING, Constants.END_INDEX_IN_FROM_DATE_STRING));
            LocalTime endTime = LocalTime.parse(deliveryHour.substring(Constants.BEGIN_INDEX_IN_TO_DATE_STRING, Constants.END_INDEX_IN_TO_DATE_STRING));
            IntervalTime time = new IntervalTime(startTime, endTime, entity);
            deliveryHours.add(time);
        }
        entity.setDeliveryHours(deliveryHours);
        return entity;
    }
}
