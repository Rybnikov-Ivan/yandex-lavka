package ru.yandex.yandexlavka.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.yandexlavka.entity.enums.CourierType;

import java.util.List;

@Data
@AllArgsConstructor
public class CourierDto {
    private Long courier_id;
    private CourierType courier_type;
    private List<Integer> regions;
    private List<String> working_hours;

    public CourierDto() {
    }
}
