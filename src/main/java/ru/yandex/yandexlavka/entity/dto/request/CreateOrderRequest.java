package ru.yandex.yandexlavka.entity.dto.request;

import lombok.Data;
import ru.yandex.yandexlavka.entity.dto.OrderDto;

import java.util.List;

@Data
public class CreateOrderRequest {
    private List<OrderDto> orders;
}
