package ru.yandex.yandexlavka.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.yandexlavka.entity.dto.CourierDto;
import ru.yandex.yandexlavka.entity.dto.OrderDto;
import ru.yandex.yandexlavka.services.assignorderservice.AssignOrderService;
import ru.yandex.yandexlavka.services.orderservice.OrderService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private AssignOrderService assignOrderService;

    @CrossOrigin
    @GetMapping(value = "/orders", produces = "application/json")
    public ResponseEntity<?> getAllOrders(
            @RequestParam(value = "offset", defaultValue = "0") @Min(0) Integer offset,
            @RequestParam(value = "limit", defaultValue = "1") @Min(1) Integer limit
    ) {
        try {
            List<OrderDto.MainOrderDto> orders = orderService.getAllOrders(offset, limit);
            if (orders.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            OrderDto.GetOrdersResponse response = new OrderDto.GetOrdersResponse(orders, offset, limit);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/orders/{order_id}", produces = "application/json")
    public ResponseEntity<?> getOrderById(@PathVariable(value = "order_id") Long orderId) {
        try {
            OrderDto.MainOrderDto order = orderService.getOrderById(orderId);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/orders", produces = "application/json")
    public ResponseEntity<?> addOrders(@RequestBody @Valid OrderDto.CreateOrderRequest request) {
        if (request == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<OrderDto.MainOrderDto> dtoList = new ArrayList<>();
        for (OrderDto.MainOrderDto dto : request.orders()) {
            try {
                dtoList.add(orderService.addOrder(dto));
            } catch (Exception ex) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        OrderDto.CreateOrderResponse response = new OrderDto.CreateOrderResponse(dtoList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping(value = "/orders/complete", produces = "application/json")
    public ResponseEntity<?> completeOrder(@RequestBody @Valid OrderDto.CompleteOrderRequest request) {
        if (request == null || request.completeOrders().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        for (OrderDto.CompleteOrderDto dto : request.completeOrders()) {
            try {
                orderService.completeOrder(dto);
            } catch (Exception ex) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        OrderDto.CompleteOrderResponse response = new OrderDto.CompleteOrderResponse(request.completeOrders());
        return new ResponseEntity<>(response.orders(), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping(value = "/orders/assign", produces = "application/json")
    public ResponseEntity<?> orderAssign(
            @RequestParam(value = "date", defaultValue = "#{T(java.time.LocalDate).now()}")
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        List<CourierDto.GetCouriersAssignOrdersResponse> response;
        try {
            response = assignOrderService.assignOrders(date);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (response.get(0).couriers().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
