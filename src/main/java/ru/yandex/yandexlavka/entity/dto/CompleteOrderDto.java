package ru.yandex.yandexlavka.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompleteOrderDto {

    @JsonProperty("courier_id")
    private Long courierId;

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("complete_time")
    private String completeTime;
}
