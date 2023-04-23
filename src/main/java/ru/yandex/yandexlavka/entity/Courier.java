package ru.yandex.yandexlavka.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.yandexlavka.entity.enums.CourierType;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "COURIERS")
public class Courier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "COURIER_TYPE", nullable = false)
    private CourierType courierType;

    @ElementCollection
    @CollectionTable(name = "REGIONS")
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

    @OneToMany(mappedBy = "courier", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Order> orders;

    public Courier() {

    }
}
