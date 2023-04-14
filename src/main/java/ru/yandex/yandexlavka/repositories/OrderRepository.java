package ru.yandex.yandexlavka.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.yandexlavka.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
