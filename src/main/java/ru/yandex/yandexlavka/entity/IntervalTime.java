package ru.yandex.yandexlavka.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@Entity(name = "yandex_lavka_IntervalTime")
@Table(name = "YANDEX_LAVKA_INTERVAL_TIME")
public class IntervalTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "START_TIME", nullable = false)
    private LocalTime startTime;

    @Column(name = "END_TIME", nullable = false)
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COURIER_ID")
    private Courier courier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    public IntervalTime(LocalTime startTime, LocalTime endTime, Courier courier) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.courier = courier;
    }

    public IntervalTime(LocalTime startTime, LocalTime endTime, Order order) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.order = order;
    }

    public IntervalTime(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public IntervalTime() {
    }
}
