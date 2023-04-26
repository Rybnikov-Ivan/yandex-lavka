package ru.yandex.yandexlavka.entity.dto;

import lombok.Data;
import ru.yandex.yandexlavka.entity.enums.CourierType;

import java.util.List;

@Data
public class CourierMetaInfoDto {
    private Long courier_id;
    private CourierType courier_type;
    private List<Integer> regions;
    private List<String> working_hours;
    private Integer rating;
    private Integer earnings;

    public CourierMetaInfoDto(Long courier_id, CourierType courier_type, List<Integer> regions, List<String> working_hours) {
        this.courier_id = courier_id;
        this.courier_type = courier_type;
        this.regions = regions;
        this.working_hours = working_hours;
    }
}
