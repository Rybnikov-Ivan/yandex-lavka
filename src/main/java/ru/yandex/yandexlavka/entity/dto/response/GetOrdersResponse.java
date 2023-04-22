package ru.yandex.yandexlavka.entity.dto.response;

import lombok.Data;
import ru.yandex.yandexlavka.entity.dto.OrderDto;

import java.util.List;

@Data
public class GetOrdersResponse {
    private List<OrderDto> orders;
    private int offset;
    private int limit;

    public GetOrdersResponse(List<OrderDto> orders, int offset, int limit) {
        this.orders = orders;
        this.offset = offset;
        this.limit = limit;
    }
}
