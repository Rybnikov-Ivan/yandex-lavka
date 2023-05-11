package ru.yandex.yandexlavka.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.yandexlavka.entity.enums.CourierType;

import java.util.List;

/**
 * Курьер
 */
@Getter
@Setter
@AllArgsConstructor
@Entity(name = "yandex_lavka_Courier")
@Table(name = "YANDEX_LAVKA_COURIER")
public class Courier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "COURIER_TYPE", nullable = false)
    private CourierType courierType;

    @ElementCollection
    @CollectionTable(name = "YANDEX_LAVKA_REGIONS")
    private List<Integer> regions;

    @OneToMany(mappedBy = "courier", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<IntervalTime> workingHours;

    @Column(name = "MAX_WEIGHT", nullable = false)
    private float maxWeight;

    @Column(name = "MAX_COUNT", nullable = false)
    private int maxCount;

    @Column(name = "FIRST_ORDER_TIME", nullable = false)
    private int firstOrderTime;

    @Column(name = "NEXT_ORDER_TIME", nullable = false)
    private int nextOrderTime;

    @Column(name = "EARNING_RATE", nullable = false)
    private int earningRate;

    @Column(name = "RATING_RATE", nullable = false)
    private int ratingRate;

    @OneToMany(mappedBy = "courier", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderGroup> orderGroups;

    public Courier() {

    }
}
