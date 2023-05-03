package ru.yandex.yandexlavka.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.yandexlavka.entity.enums.CourierType;

import java.util.List;

@Data
public class CourierDto {

    public record MainCourierDto(
            @JsonProperty("courier_id")
            Long courierId,
            @NotNull
            @JsonProperty("courier_type")
            CourierType courierType,
            @NotEmpty
            @Size(min = 1, max = 3)
            @JsonProperty("regions")
            List<@PositiveOrZero Integer> regions,
            @NotEmpty
            @JsonProperty("working_hours")
            List<@NotBlank String> workingHours) {
    }

    public record CreateCourierRequest(
            @NotEmpty
            @JsonProperty("couriers")
            List<@Valid MainCourierDto> couriers) {
    }

    public record CreateCourierResponse(
            @JsonProperty("couriers")
            List<MainCourierDto> couriers) {
    }

    public record GetCourierResponse(
            @JsonProperty("couriers")
            List<MainCourierDto> couriers,
            @JsonProperty("limit")
            int limit,
            @JsonProperty("offset")
            int offset) {
    }

    public record GetCourierMetaInfoResponse(
            @JsonProperty("courier_id")
            Long courierId,
            @JsonProperty("courier_type")
            CourierType courierType,
            @JsonProperty("regions")
            List<Integer> regions,
            @JsonProperty("working_hours")
            List<String> workingHours,
            @JsonProperty("rating")
            Integer rating,
            @JsonProperty("earnings")
            Integer earnings) {
    }

    public record GetCouriersAssignOrdersResponse(
            @JsonProperty("date")
            String date,
            @JsonProperty("couriers")
            List<OrderDto.OrdersDto> couriers) {
    }
}
