package ru.yandex.yandexlavka.entity.dto.response;

import lombok.Data;
import ru.yandex.yandexlavka.entity.dto.CompleteOrderDto;

import java.util.List;

@Data
public class CompleteOrderResponse {
    private List<CompleteOrderDto> orders;

    public CompleteOrderResponse(List<CompleteOrderDto> orders) {
        this.orders = orders;
    }
}
