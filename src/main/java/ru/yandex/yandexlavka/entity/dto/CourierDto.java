package ru.yandex.yandexlavka.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.yandexlavka.entity.enums.CourierType;

import java.util.List;

@Data
public class CourierDto {

    /**
     * Основная неперсистентная сущность курьера
     * @param courierId id
     * @param courierType тип
     * @param regions регионы
     * @param workingHours рабочие часы
     */
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

    /**
     * Запрос на создание курьеров
     * @param couriers список курьеров
     */
    public record CreateCourierRequest(
            @NotEmpty
            @JsonProperty("couriers")
            List<@Valid MainCourierDto> couriers) {
    }

    /**
     * Ответ на запрос {@link CreateCourierRequest}
     * @param couriers созданные курьеры
     */
    public record CreateCourierResponse(
            @JsonProperty("couriers")
            List<MainCourierDto> couriers) {
    }

    /**
     * Ответ на запрос получения всех курьеров
     * @param couriers курьеры
     * @param limit количество строк
     * @param offset указатель с какой строки начинать выборку
     */
    public record GetCourierResponse(
            @JsonProperty("couriers")
            List<MainCourierDto> couriers,
            @JsonProperty("limit")
            int limit,
            @JsonProperty("offset")
            int offset) {
    }

    /**
     * Ответ на расчет рейтинга и заработка курьера
     * @param courierId id курьера
     * @param courierType тип курьера
     * @param regions регионы
     * @param workingHours рабочие часы
     * @param rating рейтинг
     * @param earnings заработок
     */
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

    /**
     * Ответ на получение распределенных заказов
     * @param date дата
     * @param couriers курьеры
     */
    public record GetCouriersAssignOrdersResponse(
            @JsonProperty("date")
            String date,
            @JsonProperty("couriers")
            List<OrderDto.OrdersDto> couriers) {
    }
}
