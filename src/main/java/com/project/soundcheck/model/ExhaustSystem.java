package com.project.soundcheck.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ExhaustSystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @EqualsAndHashCode.Include
    private String name;

    private String type;

    private String material;

    @Column(columnDefinition = "TEXT")
    private String soundProfile;

    @Column(columnDefinition = "TEXT")
    private String performanceMetrics;

    @ManyToMany(mappedBy = "exhaustSystems")
    private Set<CarModel> carModels = new HashSet<>();

    @OneToMany(mappedBy = "exhaustSystem", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();
}
