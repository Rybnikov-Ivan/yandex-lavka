package ru.yandex.yandexlavka.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class CourierAssignDto {
    private Long courier_id;
    private List<OrderDto> orders;
}
