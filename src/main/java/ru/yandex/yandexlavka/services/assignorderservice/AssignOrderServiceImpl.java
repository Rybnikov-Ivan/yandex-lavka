package ru.yandex.yandexlavka.services.assignorderservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.yandexlavka.entity.Courier;
import ru.yandex.yandexlavka.entity.IntervalTime;
import ru.yandex.yandexlavka.entity.Order;
import ru.yandex.yandexlavka.entity.OrderGroup;
import ru.yandex.yandexlavka.entity.dto.CourierDto;
import ru.yandex.yandexlavka.entity.dto.OrderDto;
import ru.yandex.yandexlavka.repositories.CourierRepository;
import ru.yandex.yandexlavka.repositories.OrderGroupRepository;
import ru.yandex.yandexlavka.repositories.OrderRepository;
import ru.yandex.yandexlavka.utils.mapping.OrderMapping;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AssignOrderServiceImpl implements AssignOrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CourierRepository courierRepository;
    @Autowired
    private OrderGroupRepository orderGroupRepository;
    @Autowired
    private OrderMapping orderMapping;

    @Override
    public List<CourierDto.GetCouriersAssignOrdersResponse> assignOrders(LocalDate date) {
        List<Courier> couriers = courierRepository.findAll();
        List<Order> orders = orderRepository.findAll();

        Map<Courier, List<Order>> map = groupCourierByOrder(couriers, orders);

        for (Courier courier : map.keySet()) {
            Map<IntervalTime, List<Order>> mapIt = new HashMap<>();
            for (IntervalTime courierIt : courier.getWorkingHours()) {
                List<Order> list = new ArrayList<>();
                for (Order order : map.get(courier)) {
                    if (order.getOrderGroup() != null) {
                        continue;
                    }
                    for (IntervalTime orderIt : order.getDeliveryHours()) {
                        if (isIntersectionTime(courierIt, orderIt)) {
                            list.add(order);
                            break;
                        }
                    }
                }
                if (!list.isEmpty()) {
                    list.sort(Comparator.comparing(Order::getRegion));
                    mapIt.put(courierIt, list);
                }
            }
            List<IntervalTime> allKeys = getKeys(mapIt);

            for (IntervalTime it : allKeys) {
                List<Order> sortedOrdersByRegion = mapIt.get(it).stream()
                        .filter(o -> o.getOrderGroup() == null)
                        .sorted(Comparator.comparing(Order::getRegion))
                        .toList();
                OrderGroup orderGroup = new OrderGroup();
                List<Order> ordersForGroup = calculateGroup(courier, sortedOrdersByRegion);

                if (!ordersForGroup.isEmpty()) {
                    save(courier, orderGroup, ordersForGroup);
                }
            }
        }
        return response(couriers, date);
    }

    private List<CourierDto.GetCouriersAssignOrdersResponse> response(List<Courier> couriers, LocalDate date) {
        CourierDto.GetCouriersAssignOrdersResponse courierDto = new CourierDto.GetCouriersAssignOrdersResponse(date.toString(), new ArrayList<>());
        for (Courier courier : couriers) {
            if (courier.getOrderGroups().size() > 0) {
                OrderDto.OrdersDto orderDto = new OrderDto.OrdersDto(courier.getId(), new ArrayList<>());
                for (OrderGroup orderGroup : courier.getOrderGroups()) {
                    OrderDto.OrderGroupDto orderGroupDto = new OrderDto.OrderGroupDto(orderGroup.getId(), new ArrayList<>());
                    for (Order order : orderGroup.getOrders()) {
                        orderGroupDto.orders().add(orderMapping.mapToOrderDto(order));
                    }
                    orderDto.orderGroupDto().add(orderGroupDto);
                }
                courierDto.couriers().add(orderDto);
            }
        }
        return new ArrayList<>(List.of(courierDto));
    }

    private Map<Courier, List<Order>> groupCourierByOrder(List<Courier> couriers, List<Order> orders) {
        Map<Courier, List<Order>> map = new HashMap<>();
        for (Courier courier : couriers) {
            List<Order> list = new ArrayList<>();
            for (Order order : orders) {
                if (courier.getRegions().contains(order.getRegion()) && courier.getMaxWeight() >= order.getWeight()) {
                    boolean flag = false;
                    for (IntervalTime courierIt : courier.getWorkingHours()) {
                        if (flag) {
                            break;
                        }
                        for (IntervalTime orderIt : order.getDeliveryHours()) {
                            if (isIntersectionTime(courierIt, orderIt)) {
                                list.add(order);
                                flag = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (!list.isEmpty()) {
                map.put(courier, list);
            }
        }
        return map;
    }

    private List<IntervalTime> getKeys(Map<IntervalTime, List<Order>> mapIt) {
        Map<IntervalTime, List<Order>> linkedHashMap = mapIt.entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getValue().size()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> {throw new AssertionError();},
                        LinkedHashMap::new
                ));
        List<IntervalTime> allKeys = new ArrayList<>(linkedHashMap.keySet());
        Collections.reverse(allKeys);
        return allKeys;
    }

    private List<Order> calculateGroup(Courier courier, List<Order> sortedListByRegion) {
        float sumWeight = 0;
        int sumCount = 0;
        List<Order> orders = new ArrayList<>();
        for (Order order : sortedListByRegion) {
            if (order.getWeight() + sumWeight <= courier.getMaxWeight() && sumCount + 1 <= courier.getMaxCount()) {
                sumWeight += order.getWeight();
                sumCount += 1;
                orders.add(order);
            }
        }
        return orders;
    }

    private void save(Courier courier, OrderGroup orderGroup, List<Order> orders) {
        orderGroup.setOrders(orders);
        orderGroupRepository.save(orderGroup);
        orderGroup.setCourier(courier);
        for (Order order : orders) {
            order.setOrderGroup(orderGroup);
        }
        courier.getOrderGroups().add(orderGroup);
        courierRepository.save(courier);
    }

    private boolean isIntersectionTime(IntervalTime courierIt, IntervalTime orderIt) {
        if (orderIt.getEndTime().isBefore(courierIt.getStartTime()) || orderIt.getEndTime().equals(courierIt.getStartTime())) {
            return false;
        } else {
            return !courierIt.getEndTime().isBefore(orderIt.getStartTime()) && !courierIt.getEndTime().equals(orderIt.getStartTime());
        }
    }
}
