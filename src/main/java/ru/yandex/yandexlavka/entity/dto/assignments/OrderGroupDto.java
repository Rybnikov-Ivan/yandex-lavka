package ru.yandex.yandexlavka.entity.dto.assignments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.yandex.yandexlavka.entity.dto.OrderDto;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderGroupDto {

    @JsonProperty("group_order_id")
    Long groupId;

    @JsonProperty("orders")
    List<OrderDto> orders;

    public OrderGroupDto(Long groupId) {
        this.groupId = groupId;
        orders = new ArrayList<>();
    }
}
