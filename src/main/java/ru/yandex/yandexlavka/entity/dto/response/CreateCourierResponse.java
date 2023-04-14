package ru.yandex.yandexlavka.entity.dto.response;

import lombok.Data;
import ru.yandex.yandexlavka.entity.dto.CourierDto;

import java.util.List;

@Data
public class CreateCourierResponse {
    private List<CourierDto> couriers;

    public CreateCourierResponse(List<CourierDto> couriers) {
        this.couriers = couriers;
    }
}
