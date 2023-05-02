package ru.yandex.yandexlavka.services.courierservice;

import ru.yandex.yandexlavka.entity.dto.CourierDto;

import java.time.LocalDate;
import java.util.List;

public interface CourierService {
    List<CourierDto.MainCourierDto> getAllCouriers(int offset, int limit);
    CourierDto.MainCourierDto addCourier(CourierDto.MainCourierDto dto);
    CourierDto.MainCourierDto getCourierById(Long id);
    CourierDto.GetCouriersAssignOrdersResponse getCouriersWithOrders(LocalDate date, Long courierId);
    CourierDto.GetCourierMetaInfoResponse getRatingAndEarning(Long courierId, LocalDate startDate, LocalDate endDate);
}
