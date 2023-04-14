package ru.yandex.yandexlavka.services.orderservice;

import ru.yandex.yandexlavka.entity.dto.CompleteOrderDto;
import ru.yandex.yandexlavka.entity.dto.OrderDto;

import java.util.List;

public interface OrderService {

    List<OrderDto> getAllOrders(int offset, int limit);
    void addOrder(OrderDto OrderDto);
    OrderDto getOrderById(Long id);
    void completeOrder(CompleteOrderDto completeOrderDto);
}
