package com.sila.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private User owner;
    @NotEmpty(message ="name can't be empty")
    private String name;
    private String description;
    private String cuisineType;
    @OneToOne
    private Address address;
    @Embedded
    private ContactInformation contactInformation;
    private String openingHours;
//    @OneToMany(mappedBy = "restaurant",cascade = CascadeType.ALL,orphanRemoval = true)
//    private List<Order> orders=new ArrayList<>();
    @Column(length = 1000)
    private List<String> images;
    private LocalDateTime registrationDate;
    private boolean open;
    @JsonIgnore
    @OneToMany(mappedBy ="restaurant",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Food> foods=new ArrayList<>();

}
