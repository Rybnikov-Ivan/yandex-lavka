package ru.yandex.yandexlavka.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.yandexlavka.entity.Courier;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Long> {
}
