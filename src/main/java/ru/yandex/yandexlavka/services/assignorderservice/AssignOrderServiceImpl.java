package ru.yandex.yandexlavka.services.assignorderservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.yandexlavka.entity.Courier;
import ru.yandex.yandexlavka.entity.IntervalTime;
import ru.yandex.yandexlavka.entity.Order;
import ru.yandex.yandexlavka.entity.dto.CourierAssignDto;
import ru.yandex.yandexlavka.entity.dto.OrderDto;
import ru.yandex.yandexlavka.entity.enums.CourierType;
import ru.yandex.yandexlavka.repositories.CourierRepository;
import ru.yandex.yandexlavka.repositories.OrderRepository;
import ru.yandex.yandexlavka.utils.mapping.OrderMapping;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class AssignOrderServiceImpl implements AssignOrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CourierRepository courierRepository;
    @Autowired
    private OrderMapping orderMapping;

    @Override
    public List<CourierAssignDto> assignOrders(LocalDate date) {
        List<Courier> couriers = courierRepository.findAll();
        List<Order> orders = orderRepository.findAll();
        List<List<Courier>> listCouriers = groupCourierByType(couriers);
        HashMap<Integer, List<Order>> mapOrders = groupOrderByRegion(orders);

        for (List<Courier> couriersByType : listCouriers) {
            for (Courier courier : couriersByType) {
                Set<Integer> regions = new HashSet<>(courier.getRegions());
                for (int region : regions) {
                    assign(courier, mapOrders.get(region));
                }
            }
        }

        return getCouriersWithOrders(couriers);
    }

    private void assign(Courier courier, List<Order> orders) {
        if (orders == null) {
            return;
        }
        orders.sort(Comparator.comparingDouble(Order::getWeight));
        List<Order> ordersByCourier = courier.getOrders();
        for (Order order : orders) {
            if (order.getCourier() != null) {
                continue;
            }
            List<IntervalTime> matchingList = timeMatchingList(courier.getWorkingHours(), order.getDeliveryHours());
            if (matchingList.isEmpty()) {
                continue;
            }
            for (IntervalTime containsTime : matchingList) {
                 if (ordersByCourier.isEmpty()) {
                    saveIfEmpty(courier, order, containsTime);
                    break;
                } else {
                    List<Order> ordersInMatchingTime = ordersByCourier.stream()
                            .filter(o ->
                                    ((containsTime.getStartTime().isBefore(o.getAssignTime())) ||
                                    containsTime.getStartTime().equals(o.getAssignTime())) &&
                                    ((containsTime.getEndTime().isAfter(o.getAssignTime())) ||
                                    containsTime.getEndTime().equals(o.getAssignTime())))
                            .toList();
                    if (ordersInMatchingTime.isEmpty()) {
                        saveIfEmpty(courier, order, containsTime);
                        break;
                    }
                    for (int i = 0; i < ordersInMatchingTime.size(); i++) {
                        LocalTime assignTime = ordersInMatchingTime.get(i).getAssignTime();
                        if (i == ordersInMatchingTime.size() - 1) {
                            order.setAssignTime(assignTime.plus(courier.getNextOrderTime(), ChronoUnit.MINUTES));
                            save(courier, order);
                        }
                    }
                    if (order.getCourier() != null) {
                        break;
                    }
                }
            }
        }
    }

    private void saveIfEmpty(Courier courier, Order order, IntervalTime containsTime) {
        if (order.getWeight() <= courier.getMaxWeight()) {
            LocalTime assignTime = containsTime.getStartTime().plus(courier.getFirstOrderTime(), ChronoUnit.MINUTES);
            order.setAssignTime(assignTime);
            save(courier, order);
        }
    }

    private List<IntervalTime> timeMatchingList(List<IntervalTime> workingHours, List<IntervalTime> deliveryHours) {
        List<IntervalTime> mergeList = new ArrayList<>();
        for (IntervalTime workingHour : workingHours) {
            LocalTime beginWork = workingHour.getStartTime();
            LocalTime endWork = workingHour.getEndTime();
            for (IntervalTime deliveryHour : deliveryHours) {
                LocalTime beginDelivery = deliveryHour.getStartTime();
                LocalTime endDelivery = deliveryHour.getEndTime();

                LocalTime[] matching = currentTimeBetween(beginWork, endWork, beginDelivery, endDelivery);
                if (matching != null && !matching[0].equals(matching[1]) && matching[0].isBefore(matching[1])) {
                    mergeList.add(new IntervalTime(matching[0], matching[1]));
                }
            }
        }
        return mergeList;
    }

    private LocalTime[] currentTimeBetween(LocalTime beginWork, LocalTime endWork, LocalTime beginDelivery, LocalTime endDelivery) {
        if ((beginDelivery.isBefore(beginWork) || beginDelivery.equals(beginWork)) &&
                (endDelivery.isAfter(endWork) || endDelivery.equals(endWork))) {

            return new LocalTime[]{beginWork, endWork};
        } else if ((beginDelivery.isBefore(beginWork) || beginDelivery.equals(beginWork)) &&
                (endDelivery.isBefore(endWork) || endDelivery.equals(endWork))) {

            return new LocalTime[]{beginWork, endDelivery};
        } else if ((beginDelivery.isAfter(beginWork) || beginDelivery.equals(beginWork)) &&
                (endDelivery.isBefore(endWork) || endDelivery.equals(endWork))) {

            return new LocalTime[]{beginDelivery, endDelivery};
        } else if ((beginDelivery.isAfter(beginWork) || beginDelivery.equals(beginWork)) &&
                (endDelivery.isAfter(endWork) || endDelivery.equals(endWork))) {

            return new LocalTime[]{beginDelivery, endWork};
        } else {
            return null;
        }
    }
    private List<List<Courier>> groupCourierByType(List<Courier> couriers) {
        List<List<Courier>> list = new ArrayList<>();
        List<Courier> autoCouriers = couriers.stream()
                .filter(o -> o.getCourierType().equals(CourierType.AUTO))
                .toList();
        list.add(autoCouriers);

        List<Courier> bikeCouriers = couriers.stream()
                .filter(o -> o.getCourierType().equals(CourierType.BIKE))
                .toList();
        list.add(bikeCouriers);

        List<Courier> footCouriers = couriers.stream()
                .filter(o -> o.getCourierType().equals(CourierType.FOOT))
                .toList();
        list.add(footCouriers);

        return list;
    }

    private HashMap<Integer, List<Order>> groupOrderByRegion(List<Order> orders) {
        HashMap<Integer, List<Order>> map = new HashMap<>();
        for (Order order : orders) {
            int key = order.getRegion();
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
            }
            map.get(key).add(order);
        }

        return map;
    }

    private void save(Courier courier, Order order) {
        order.setCourier(courier);
        courier.getOrders().add(order);
        orderRepository.save(order);
        courierRepository.save(courier);
    }

    private List<CourierAssignDto> getCouriersWithOrders(List<Courier> couriers) {
        List<CourierAssignDto> courierAssignDtoList = new ArrayList<>();
        for (Courier courier : couriers) {
            if (courier.getOrders().size() > 0) {
                CourierAssignDto dto = new CourierAssignDto();
                dto.setCourier_id(courier.getId());
                List<OrderDto> orderDtoList = new ArrayList<>();
                for (Order order : courier.getOrders()) {
                    orderDtoList.add(orderMapping.mapToOrderDto(order));
                }
                dto.setOrders(orderDtoList);
                courierAssignDtoList.add(dto);
            }
        }
        return courierAssignDtoList;
    }
}
