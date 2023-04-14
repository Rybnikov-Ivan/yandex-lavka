package ru.yandex.yandexlavka.entity.dto.request;

import lombok.Data;
import ru.yandex.yandexlavka.entity.dto.CourierDto;

import java.util.List;

@Data
public class CreateCourierRequest {
    private List<CourierDto> couriers;
}
