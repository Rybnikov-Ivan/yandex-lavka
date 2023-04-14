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

    public CourierDto mapToCourierDto(Courier courier) {
        CourierDto dto = new CourierDto();
        dto.setCourier_id(courier.getId());
        dto.setCourier_type(courier.getCourierType());
        dto.setRegions(courier.getRegions());
        List<String> workingHours = new ArrayList<>();
        for (IntervalTime time : courier.getWorkingHours()) {
            workingHours.add(time.getStartTime() + "-" + time.getEndTime());
        }
        dto.setWorking_hours(workingHours);
        return dto;
    }

    public Courier mapToCourierEntity(CourierDto dto) {
        Courier entity = new Courier();
        entity.setId(dto.getCourier_id());
        entity.setCourierType(dto.getCourier_type());
        entity.setRegions(dto.getRegions());
        List<IntervalTime> workingHours = new ArrayList<>();
        for (String workingHour : dto.getWorking_hours()) {
            LocalTime startTime = LocalTime.parse(workingHour.substring(Constants.BEGIN_INDEX_IN_FROM_DATE_STRING, Constants.END_INDEX_IN_FROM_DATE_STRING));
            LocalTime endTime = LocalTime.parse(workingHour.substring(Constants.BEGIN_INDEX_IN_TO_DATE_STRING, Constants.END_INDEX_IN_TO_DATE_STRING));
            IntervalTime time = new IntervalTime(startTime, endTime, entity);
            workingHours.add(time);
        }
        entity.setWorkingHours(workingHours);
        return entity;
    }
}