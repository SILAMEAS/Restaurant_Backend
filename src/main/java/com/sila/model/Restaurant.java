package com.sila.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sila.model.image.ImageRestaurant;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JsonIgnore
    private User owner;
    @NotEmpty(message = "name can't be empty")
    private String name;
    private String description;
    private String cuisineType;
    @ManyToOne
    @JoinColumn(name = "address_id", nullable = true)
    private Address address;
    @Embedded
    private ContactInformation contactInformation;
    private String openingHours;
    private LocalDateTime registrationDate;
    private boolean open;
    @JsonIgnore
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Food> foods = new ArrayList<>();
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ImageRestaurant> images = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<Favorite> favorites;

    private int rating=0;

    public void addImage(ImageRestaurant restaurant) {
        this.images.add(restaurant);
    }

    public double deliveryFee = 0;

    private double restaurantDiscount = 0;
}
