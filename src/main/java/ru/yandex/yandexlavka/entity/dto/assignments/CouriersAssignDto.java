package ru.yandex.yandexlavka.entity.dto.assignments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CouriersAssignDto {

    @JsonProperty("date")
    private String date;

    @JsonProperty("couriers")
    private List<OrdersDto> couriers;

    public CouriersAssignDto(String date) {
        this.date = date;
        couriers = new ArrayList<>();
    }
}
