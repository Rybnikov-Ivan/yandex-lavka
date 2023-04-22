package ru.yandex.yandexlavka.services.assignorderservice;

import ru.yandex.yandexlavka.entity.dto.CourierAssignDto;

import java.time.LocalDate;
import java.util.List;

public interface AssignOrderService {
    List<CourierAssignDto> assignOrders(LocalDate date);
}
