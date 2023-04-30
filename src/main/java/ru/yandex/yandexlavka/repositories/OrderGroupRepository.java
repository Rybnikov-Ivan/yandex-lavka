package ru.yandex.yandexlavka.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.yandexlavka.entity.OrderGroup;

@Repository
public interface OrderGroupRepository extends JpaRepository<OrderGroup, Long> {
}
