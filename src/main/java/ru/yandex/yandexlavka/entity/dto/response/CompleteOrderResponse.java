package ru.yandex.yandexlavka.entity.dto.response;

import lombok.Data;
import ru.yandex.yandexlavka.entity.dto.OrderDto;

import java.util.List;

@Data
public class CompleteOrderResponse {
    private List<OrderDto> orders;
}
