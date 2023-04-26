package ru.yandex.yandexlavka.services.courierservice;

import ru.yandex.yandexlavka.entity.dto.assignments.CouriersAssignDto;
import ru.yandex.yandexlavka.entity.dto.CourierDto;
import ru.yandex.yandexlavka.entity.dto.CourierMetaInfoDto;

import java.time.LocalDate;
import java.util.List;

public interface CourierService {
    List<CourierDto> getAllCouriers(int offset, int limit);
    void addCourier(CourierDto dto);
    CourierDto getCourierById(Long id);
    CouriersAssignDto getCouriersWithOrders(LocalDate date, Long courierId);
    CourierMetaInfoDto getRatingAndEarning(Long courierId, LocalDate startDate, LocalDate endDate);
}
