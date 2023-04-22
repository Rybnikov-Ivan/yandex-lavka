package ru.yandex.yandexlavka.services.courierservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.yandex.yandexlavka.entity.Courier;
import ru.yandex.yandexlavka.entity.dto.CourierDto;
import ru.yandex.yandexlavka.repositories.CourierRepository;
import ru.yandex.yandexlavka.utils.mapping.CourierMapping;

import java.util.List;

@Service
public class CourierServiceImpl implements CourierService{
    @Autowired
    private CourierRepository courierRepository;
    @Autowired
    private CourierMapping courierMapping;
    @Override
    public List<CourierDto> getAllCouriers(int offset, int limit) {
        return courierRepository.findAll(PageRequest.of(offset, limit))
                .stream().map(courierMapping::mapToCourierDto)
                .toList();
    }

    @Override
    public void addCourier(CourierDto dto) {
        Courier courier = courierMapping.mapToCourierEntity(dto);
        courierRepository.save(courier);
        dto.setCourier_id(courier.getId());
    }

    @Override
    public CourierDto getCourierById(Long id) {
        return courierMapping.mapToCourierDto(
                courierRepository.findById(id)
                        .orElse(new Courier()));
    }

}
