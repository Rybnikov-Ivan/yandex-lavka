package ru.yandex.yandexlavka.services.orderservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.yandex.yandexlavka.entity.Courier;
import ru.yandex.yandexlavka.entity.Order;
import ru.yandex.yandexlavka.entity.dto.CompleteOrderDto;
import ru.yandex.yandexlavka.entity.dto.OrderDto;
import ru.yandex.yandexlavka.repositories.CourierRepository;
import ru.yandex.yandexlavka.repositories.OrderRepository;
import ru.yandex.yandexlavka.utils.mapping.OrderMapping;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CourierRepository courierRepository;
    @Autowired
    private OrderMapping orderMapping;
    @Override
    public List<OrderDto> getAllOrders(int offset, int limit) {
        return orderRepository.findAll(PageRequest.of(offset, limit)).stream()
                .map(orderMapping::mapToOrderDto)
                .toList();

    }

    @Override
    public void addOrder(OrderDto orderDto) {
        Order order = orderMapping.mapToOrderEntity(orderDto);
        orderRepository.save(order);
        orderDto.setOrder_id(order.getId());
    }

    @Override
    public OrderDto getOrderById(Long id) {
        return orderMapping.mapToOrderDto(
                orderRepository.findById(id)
                        .orElse(new Order()));
    }

    @Override
    public void completeOrder(CompleteOrderDto completeOrderDto) {
        Courier courier = courierRepository.findById(completeOrderDto.getCourier_id()).orElse(null);
        Order order = orderRepository.findById(completeOrderDto.getOrder_id()).orElse(null);
        if (courier == null || order == null) {
            throw new RuntimeException();
        }
        if (courier.getOrders().contains(order)) {
            order.setCompletedTime(LocalDateTime.parse(completeOrderDto.getComplete_time()));
        } else {
            throw new RuntimeException();
        }
    }
}
