package ru.yandex.yandexlavka.entity.dto.response;

import lombok.Data;
import ru.yandex.yandexlavka.entity.dto.CourierDto;

import java.util.List;

@Data
public class GetCouriersResponse {
    private List<CourierDto> couriers;
    private int limit;
    private int offset;

    public GetCouriersResponse(List<CourierDto> couriers, int offset, int limit) {
        this.couriers = couriers;
        this.offset = offset;
        this.limit = limit;
    }
}
