package ru.yandex.yandexlavka.controllers;

import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.yandexlavka.entity.dto.CourierAssignDto;
import ru.yandex.yandexlavka.entity.dto.CourierDto;
import ru.yandex.yandexlavka.entity.dto.CourierMetaInfoDto;
import ru.yandex.yandexlavka.entity.dto.request.CreateCourierRequest;
import ru.yandex.yandexlavka.entity.dto.response.AssignOrdersResponse;
import ru.yandex.yandexlavka.entity.dto.response.CreateCourierResponse;
import ru.yandex.yandexlavka.entity.dto.response.GetCouriersResponse;
import ru.yandex.yandexlavka.services.courierservice.CourierService;

import java.time.LocalDate;
import java.util.List;

@RestController
public class CourierController {
    @Autowired
    CourierService courierService;

    @CrossOrigin
    @GetMapping(value = "/couriers", produces = "application/json")
    public ResponseEntity<?> getAllCouriers(
            @RequestParam(value = "offset", defaultValue = "0") @Min(0) Integer offset,
            @RequestParam(value = "limit", defaultValue = "1") @Min(1) Integer limit
    ) {
        try {
            List<CourierDto> couriers = courierService.getAllCouriers(offset, limit);
            GetCouriersResponse response = new GetCouriersResponse(couriers, offset, limit);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/couriers/{courier_id}", produces = "application/json")
    public ResponseEntity<?> getCourierById(@PathVariable Long id) {
        try {
            CourierDto courier = courierService.getCourierById(id);
            return new ResponseEntity<>(courier, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/couriers", produces = "application/json")
    public ResponseEntity<?> addCouriers(@RequestBody CreateCourierRequest request) {
        if (request == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        for (CourierDto dto : request.getCouriers()) {
            try {
                courierService.addCourier(dto);
            } catch (Exception ex) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        CreateCourierResponse response = new CreateCourierResponse(request.getCouriers());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = "/couriers/assignments", produces = "application/json")
    public ResponseEntity<?> getAssignmentsCouriers(
            @RequestParam(value = "date", defaultValue = "#{T(java.time.LocalDate).now()}")
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(value = "courier_id", defaultValue = "0") Long courierId) {
        List<CourierAssignDto> couriersDto;
        try {
            couriersDto = courierService.getCouriersWithOrders(date, courierId);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AssignOrdersResponse response = new AssignOrdersResponse(date, couriersDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = "/couriers/meta-info/{courier_id}", produces = "application/json")
    public ResponseEntity<?> getRatingOfCourier(
            @PathVariable Long courier_id,
            @RequestParam(value = "start_date")
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(value = "end_date")
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate)
    {
        CourierMetaInfoDto response;
        try {
            response = courierService.getRatingAndEarning(courier_id, startDate, endDate);
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
