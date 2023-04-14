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

    @OneToMany(mappedBy = "courier", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Order> orders;

    public Courier() {

    }
}
