package ru.yandex.yandexlavka.entity.dto.request;

import lombok.Data;
import ru.yandex.yandexlavka.entity.dto.CompleteOrderDto;

import java.util.List;

@Data
public class CompleteOrderRequest {
    private List<CompleteOrderDto> complete_info;
}
