package ru.yandex.yandexlavka.entity.dto.response;

import lombok.Data;
import ru.yandex.yandexlavka.entity.dto.CourierAssignDto;

import java.time.LocalDate;
import java.util.List;

@Data
public class AssignOrdersResponse {
    private LocalDate date;
    private List<CourierAssignDto> couriers;

    public AssignOrdersResponse(LocalDate date, List<CourierAssignDto> couriers) {
        this.date = date;
        this.couriers = couriers;
    }
}
