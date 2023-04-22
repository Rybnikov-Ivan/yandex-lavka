package ru.yandex.yandexlavka.entity.dto.response;

import lombok.Data;
import ru.yandex.yandexlavka.entity.dto.OrderDto;

import java.util.List;

@Data
public class CreateOrderResponse {
    private List<OrderDto> orders;

    public CreateOrderResponse(List<OrderDto> orders) {
        this.orders = orders;
    }
}
