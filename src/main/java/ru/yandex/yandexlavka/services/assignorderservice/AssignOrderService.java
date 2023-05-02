package ru.yandex.yandexlavka.services.assignorderservice;

import ru.yandex.yandexlavka.entity.dto.CourierDto;

import java.time.LocalDate;
import java.util.List;

public interface AssignOrderService {
    List<CourierDto.GetCouriersAssignOrdersResponse> assignOrders(LocalDate date);
}
