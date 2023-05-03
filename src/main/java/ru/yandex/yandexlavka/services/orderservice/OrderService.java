package ru.yandex.yandexlavka.services.orderservice;

import ru.yandex.yandexlavka.entity.dto.OrderDto;

import java.util.List;

public interface OrderService {

    List<OrderDto.MainOrderDto> getAllOrders(int offset, int limit);
    OrderDto.MainOrderDto addOrder(OrderDto.MainOrderDto OrderDto);
    OrderDto.MainOrderDto getOrderById(Long id);
    void completeOrder(OrderDto.CompleteOrderDto completeOrderDto);
}
