package ru.yandex.yandexlavka.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Entity(name = "yandex_lavka_Order")
@Table(name = "YANDEX_LAVKA_ORDER")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID", nullable = false)
    private Long id;

    @Column(name = "COST", nullable = false)
    private BigDecimal cost;

    @Column(name = "WEIGHT", nullable = false)
    private Float weight;

    @Column(name = "REGION", nullable = false)
    private Integer region;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<IntervalTime> deliveryHours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_GROUP_ID")
    private OrderGroup orderGroup;

    @Column(name = "COMPLETED_TIME")
    private LocalDateTime completedTime;

    public Order() {

    }
}
