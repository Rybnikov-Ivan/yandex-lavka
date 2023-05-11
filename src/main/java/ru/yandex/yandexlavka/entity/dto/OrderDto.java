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

    /**
     * Основная неперсистентная сущность заказа
     * @param orderId id
     * @param weight вес
     * @param region регион
     * @param deliveryHours рабочие часы
     * @param cost цена
     * @param completedTime время выполнения заказа
     */
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

    /**
     * Ответ на запрос получения всех заказов
     * @param orders заказы
     * @param offset количество строк
     * @param limit указатель с какой строки начинать выборку
     */
    public record GetOrdersResponse(
            @JsonProperty("orders")
            List<MainOrderDto> orders,
            @JsonProperty("offset")
            Integer offset,
            @JsonProperty("limit")
            Integer limit
    ) { }

    /**
     * Запрос на создание заказов
     * @param orders заказы
     */
    public record CreateOrderRequest(
            @NotEmpty
            @JsonProperty("orders")
            List<@Valid MainOrderDto> orders
    ) { }

    /**
     * Ответ на запрос {@link CreateOrderRequest}
     * @param orders заказы
     */
    public record CreateOrderResponse(
            @JsonProperty("orders")
            List<MainOrderDto> orders
    ) { }

    /**
     * Запрос на выполнение заказа
     * @param courierId id курьера
     * @param orderId id заказа
     * @param completeTime время выполнения
     */
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

    /**
     * Запрос на получение информации о курьерах
     * @param completeOrders
     */
    public record CompleteOrderRequest(
            @JsonProperty("complete_info")
            List<@Valid CompleteOrderDto> completeOrders
    ) { }

    /**
     * Ответ на запрос {@link CompleteOrderRequest}
     * @param orders заказы
     */
    public record CompleteOrderResponse(
            @JsonProperty("orders")
            List<CompleteOrderDto> orders
    ) { }

    /**
     * Группа заказов
     * @param groupId id
     * @param orders заказы
     */
    public record OrderGroupDto(
            @JsonProperty("group_order_id")
            Long groupId,
            @JsonProperty("orders")
            List<MainOrderDto> orders
    ) { }

    /**
     * Заказ
     * @param courierId
     * @param orderGroupDto
     */
    public record OrdersDto(
            @JsonProperty("courier_id")
            Long courierId,

            @JsonProperty("orders")
            List<OrderGroupDto> orderGroupDto
    ) { }
}
