package ru.yandex.yandexlavka.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.yandex.yandexlavka.entity.enums.CourierType;

import java.util.List;

@Data
public class CourierMetaInfoDto {
    @JsonProperty("courier_id")
    private Long courierId;

    @JsonProperty("courier_type")
    private CourierType courierType;

    @JsonProperty("regions")
    private List<Integer> regions;

    @JsonProperty("working_hours")
    private List<String> workingHours;

    @JsonProperty("rating")
    private Integer rating;

    @JsonProperty("earnings")
    private Integer earnings;

    public CourierMetaInfoDto(Long courierId, CourierType courierType, List<Integer> regions, List<String> workingHours) {
        this.courierId = courierId;
        this.courierType = courierType;
        this.regions = regions;
        this.workingHours = workingHours;
    }
}
