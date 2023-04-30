package ru.yandex.yandexlavka.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.yandexlavka.entity.enums.CourierType;

import java.util.List;

@Data
@AllArgsConstructor
public class CourierDto {
    @JsonProperty("courier_id")
    private Long courierId;

    @JsonProperty("courier_type")
    private CourierType courierType;

    @JsonProperty("regions")
    private List<Integer> regions;

    @JsonProperty("working_hours")
    private List<String> workingHours;

    public CourierDto() {
    }
}
