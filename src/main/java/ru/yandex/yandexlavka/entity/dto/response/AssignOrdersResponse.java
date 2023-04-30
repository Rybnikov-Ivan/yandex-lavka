package ru.yandex.yandexlavka.entity.dto.response;

import lombok.Data;
import ru.yandex.yandexlavka.entity.dto.assignments.CouriersAssignDto;

import java.time.LocalDate;
import java.util.List;

@Data
public class AssignOrdersResponse {
    private LocalDate date;
    private List<CouriersAssignDto> couriers;

    public AssignOrdersResponse(LocalDate date, List<CouriersAssignDto> couriers) {
        this.date = date;
        this.couriers = couriers;
    }
}
