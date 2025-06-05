package com.sila.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sila.model.image.ImageFood;
import com.sila.util.enums.FoodType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double price;

    private boolean available;

    private double tax = 0;
    private double discount = 0;
    private double priceWithDiscount = 0.0; // ✅ Stored in DB

    private Date creationDate;

    @Enumerated(EnumType.STRING)
    private FoodType foodtype;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ImageFood> images = new ArrayList<>();

    @Transient
    public double getPriceWithDiscountCalculated() {
        double foodDiscount = this.discount;
        double restaurantDiscount = (restaurant != null) ? restaurant.getRestaurantDiscount() : 0;
        double totalDiscount = Math.min(foodDiscount + restaurantDiscount, 100);
        return Math.round((price - price * totalDiscount / 100.0) * 100.0) / 100.0;
    }

    @Transient
    public double getTotalDiscount() {
        double foodDiscount = this.discount;
        double restaurantDiscount = (restaurant != null) ? restaurant.getRestaurantDiscount() : 0;
        return foodDiscount + restaurantDiscount;
    }
    @Transient
    public double getPriceWithDiscount() {
        return getPriceWithDiscountCalculated();
    }

    // Getters, setters, and builder as needed
}
