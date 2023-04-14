package ru.yandex.yandexlavka.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompleteOrderDto {
    private Long courier_id;
    private Long order_id;
    private String complete_time;
}
