package ru.yandex.yandexlavka.utils.mapping;

import org.springframework.stereotype.Service;
import ru.yandex.yandexlavka.entity.Courier;
import ru.yandex.yandexlavka.entity.IntervalTime;
import ru.yandex.yandexlavka.entity.dto.CourierDto;
import ru.yandex.yandexlavka.utils.Constants;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourierMapping {

    public CourierDto.MainCourierDto mapToCourierDto(Courier courier) {
        List<String> workingHours = new ArrayList<>();
        for (IntervalTime time : courier.getWorkingHours()) {
            workingHours.add(time.getStartTime() + "-" + time.getEndTime());
        }
        return new CourierDto.MainCourierDto(courier.getId(), courier.getCourierType(), courier.getRegions(), workingHours);
    }

    public Courier mapToCourierEntity(CourierDto.MainCourierDto dto) {
        Courier entity = new Courier();
        entity.setId(dto.courierId());
        entity.setCourierType(dto.courierType());
        entity.setRegions(dto.regions());
        List<IntervalTime> workingHours = new ArrayList<>();
        for (String workingHour : dto.workingHours()) {
            LocalTime startTime = LocalTime.parse(workingHour.substring(Constants.BEGIN_INDEX_IN_FROM_DATE_STRING, Constants.END_INDEX_IN_FROM_DATE_STRING));
            LocalTime endTime = LocalTime.parse(workingHour.substring(Constants.BEGIN_INDEX_IN_TO_DATE_STRING, Constants.END_INDEX_IN_TO_DATE_STRING));
            IntervalTime time = new IntervalTime(startTime, endTime, entity);
            workingHours.add(time);
        }
        entity.setWorkingHours(workingHours);
        return entity;
    }
}
