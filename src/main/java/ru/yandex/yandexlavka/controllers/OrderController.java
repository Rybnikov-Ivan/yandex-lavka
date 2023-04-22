package ru.yandex.yandexlavka.controllers;

import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.yandexlavka.entity.dto.CompleteOrderDto;
import ru.yandex.yandexlavka.entity.dto.CourierAssignDto;
import ru.yandex.yandexlavka.entity.dto.OrderDto;
import ru.yandex.yandexlavka.entity.dto.request.CompleteOrderRequest;
import ru.yandex.yandexlavka.entity.dto.request.CreateOrderRequest;
import ru.yandex.yandexlavka.entity.dto.response.AssignOrdersResponse;
import ru.yandex.yandexlavka.entity.dto.response.CompleteOrderResponse;
import ru.yandex.yandexlavka.entity.dto.response.CreateOrderResponse;
import ru.yandex.yandexlavka.entity.dto.response.GetOrdersResponse;
import ru.yandex.yandexlavka.services.assignorderservice.AssignOrderService;
import ru.yandex.yandexlavka.services.orderservice.OrderService;

import java.time.LocalDate;
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
            List<OrderDto> orders = orderService.getAllOrders(offset, limit);
            GetOrdersResponse response = new GetOrdersResponse(orders, offset, limit);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/orders/{id}", produces = "application/json")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        try {
            OrderDto order = orderService.getOrderById(id);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/orders", produces = "application/json")
    public ResponseEntity<?> addOrders(@RequestBody CreateOrderRequest request) {
        if (request == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        for (OrderDto dto : request.getOrders()) {
            try {
                orderService.addOrder(dto);
            } catch (Exception ex) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        CreateOrderResponse response = new CreateOrderResponse(request.getOrders());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping(value = "/orders/complete", produces = "application/json")
    public ResponseEntity<?> completeOrder(@RequestBody CompleteOrderRequest request) {
        if (request == null || request.getComplete_info().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        for (CompleteOrderDto dto : request.getComplete_info()) {
            try {
                orderService.completeOrder(dto);
            } catch (Exception ex) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        CompleteOrderResponse response = new CompleteOrderResponse(request.getComplete_info());
        return new ResponseEntity<>(response.getOrders(), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping(value = "/orders/assign", produces = "application/json")
    public ResponseEntity<?> orderAssign(
            @RequestParam(value = "date", defaultValue = "#{T(java.time.LocalDate).now()}")
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        List<CourierAssignDto> courierList;
        try {
            courierList = assignOrderService.assignOrders(date);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AssignOrdersResponse response = new AssignOrdersResponse(date, courierList);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
