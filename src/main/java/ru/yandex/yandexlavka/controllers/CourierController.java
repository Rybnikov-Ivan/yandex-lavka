package ru.yandex.yandexlavka.controllers;

import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.yandexlavka.entity.dto.CourierDto;
import ru.yandex.yandexlavka.services.courierservice.CourierService;

import java.time.LocalDate;
import java.util.ArrayList;
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
            List<CourierDto.MainCourierDto> couriers = courierService.getAllCouriers(offset, limit);
            if (couriers.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            CourierDto.GetCourierResponse response = new CourierDto.GetCourierResponse(couriers, offset, limit);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/couriers/{courier_id}", produces = "application/json")
    public ResponseEntity<?> getCourierById(@PathVariable(value = "courier_id") Long courierId) {
        try {
            CourierDto.MainCourierDto response = courierService.getCourierById(courierId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/couriers", produces = "application/json")
    public ResponseEntity<?> addCouriers(@RequestBody CourierDto.CreateCourierRequest request) {
        if (request == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<CourierDto.MainCourierDto> dtoList = new ArrayList<>();
        for (CourierDto.MainCourierDto dto : request.couriers()) {
            try {
                dtoList.add(courierService.addCourier(dto));
            } catch (RuntimeException ex) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        CourierDto.CreateCourierResponse response = new CourierDto.CreateCourierResponse(dtoList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = "/couriers/assignments", produces = "application/json")
    public ResponseEntity<?> getAssignmentsCouriers(
            @RequestParam(value = "date", defaultValue = "#{T(java.time.LocalDate).now()}")
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(value = "courier_id", defaultValue = "0") Long courierId) {
        CourierDto.GetCouriersAssignOrdersResponse response;
        try {
            response = courierService.getCouriersWithOrders(date, courierId);
            if (response.couriers().isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = "/couriers/meta-info/{courier_id}", produces = "application/json")
    public ResponseEntity<?> getRatingOfCourier(
            @PathVariable(value = "courier_id") Long courierId,
            @RequestParam(value = "start_date")
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(value = "end_date")
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate)
    {
        CourierDto.GetCourierMetaInfoResponse response;
        try {
            response = courierService.getRatingAndEarning(courierId, startDate, endDate);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
