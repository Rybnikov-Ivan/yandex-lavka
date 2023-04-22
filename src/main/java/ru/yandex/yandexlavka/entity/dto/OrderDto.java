package ru.yandex.yandexlavka.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderDto {
    private Long order_id;
    private Float weight;
    private Integer regions;
    private List<String> delivery_hours;
    private Integer cost;
    private String completed_time;
    public OrderDto() {}
}
