package ru.yandex.yandexlavka.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderDto {
    public record MainOrderDto(
            @JsonProperty("order_id")
            Long orderId,
            @JsonProperty("weight")
            Float weight,
            @JsonProperty("regions")
            Integer region,
            @JsonProperty("delivery_hours")
            List<String> deliveryHours,
            @JsonProperty("cost")
            Integer cost,
            @JsonProperty("completed_time")
            String completedTime
    ) { }

    public record GetOrdersResponse(
            @JsonProperty("orders")
            List<MainOrderDto> orders,
            @JsonProperty("offset")
            Integer offset,
            @JsonProperty("limit")
            Integer limit
    ) { }

    public record CreateOrderRequest(
            @JsonProperty("orders")
            List<MainOrderDto> orders
    ) { }

    public record CreateOrderResponse(
            @JsonProperty("orders")
            List<MainOrderDto> orders
    ) { }

    public record CompleteOrderDto(
            @JsonProperty("courier_id")
            Long courierId,
            @JsonProperty("order_id")
            Long orderId,
            @JsonProperty("complete_time")
            String completeTime
    ) { }

    public record CompleteOrderRequest(
            @JsonProperty("complete_info")
            List<CompleteOrderDto> completeOrders
    ) { }

    public record CompleteOrderResponse(
            @JsonProperty("orders")
            List<CompleteOrderDto> orders
    ) { }

    public record OrderGroupDto(
            @JsonProperty("group_order_id")
            Long groupId,
            @JsonProperty("orders")
            List<MainOrderDto> orders
    ) { }

    public record OrdersDto(
            @JsonProperty("courier_id")
            Long courierId,

            @JsonProperty("orders")
            List<OrderGroupDto> orderGroupDto
    ) { }
}
