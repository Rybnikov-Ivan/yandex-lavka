package ru.yandex.yandexlavka.services.orderservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.yandex.yandexlavka.entity.Courier;
import ru.yandex.yandexlavka.entity.Order;
import ru.yandex.yandexlavka.entity.OrderGroup;
import ru.yandex.yandexlavka.entity.dto.CompleteOrderDto;
import ru.yandex.yandexlavka.entity.dto.OrderDto;
import ru.yandex.yandexlavka.repositories.CourierRepository;
import ru.yandex.yandexlavka.repositories.OrderRepository;
import ru.yandex.yandexlavka.utils.mapping.OrderMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        Courier courier = courierRepository.findById(completeOrderDto.getCourierId()).orElse(null);
        Order order = orderRepository.findById(completeOrderDto.getOrderId()).orElse(null);
        if (courier == null || order == null) {
            throw new RuntimeException();
        }
        boolean isCompleted = false;
        for (OrderGroup orderGroup : courier.getOrderGroups()) {
            if (orderGroup.getOrders().contains(order) && order.getCompletedTime() == null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                order.setCompletedTime(LocalDateTime.parse(completeOrderDto.getCompleteTime(), formatter));
                orderRepository.save(order);
                isCompleted = true;
                break;
            }
        }
        if (!isCompleted) {
            throw new RuntimeException();
        }
    }
}
