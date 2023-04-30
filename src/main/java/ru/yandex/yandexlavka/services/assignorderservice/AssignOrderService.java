package ru.yandex.yandexlavka.services.assignorderservice;

import ru.yandex.yandexlavka.entity.dto.assignments.CouriersAssignDto;

import java.time.LocalDate;
import java.util.List;

public interface AssignOrderService {
    List<CouriersAssignDto> assignOrders(LocalDate date);
}
