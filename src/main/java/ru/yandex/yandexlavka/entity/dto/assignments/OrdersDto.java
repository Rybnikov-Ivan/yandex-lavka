package ru.yandex.yandexlavka.entity.dto.assignments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrdersDto {
    @JsonProperty("courier_id")
    private Long courier_id;

    @JsonProperty("orders")
    List<OrderGroupDto> orderGroupDto;

    public OrdersDto(Long courier_id) {
        this.courier_id = courier_id;
        orderGroupDto = new ArrayList<>();
    }
}
