package ru.yandex.yandexlavka.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderDto {
    public record MainOrderDto(
            @JsonProperty("order_id")
            Long orderId,
            @PositiveOrZero
            @JsonProperty("weight")
            Float weight,
            @PositiveOrZero
            @JsonProperty("regions")
            Integer region,
            @NotEmpty
            @JsonProperty("delivery_hours")
            List<@NotBlank String> deliveryHours,
            @Positive
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
            @NotEmpty
            @JsonProperty("orders")
            List<@Valid MainOrderDto> orders
    ) { }

    public record CreateOrderResponse(
            @JsonProperty("orders")
            List<MainOrderDto> orders
    ) { }

    public record CompleteOrderDto(
            @NotNull
            @PositiveOrZero
            @JsonProperty("courier_id")
            Long courierId,
            @NotNull
            @PositiveOrZero
            @JsonProperty("order_id")
            Long orderId,
            @NotBlank
            @JsonProperty("complete_time")
            String completeTime
    ) {
    }

    public record CompleteOrderRequest(
            @JsonProperty("complete_info")
            List<@Valid CompleteOrderDto> completeOrders
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
