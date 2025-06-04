package com.sila.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sila.model.image.ImageFood;
import com.sila.util.enums.FoodType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@ToString
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ImageFood> images = new ArrayList<>();
    private boolean available;
    @ManyToOne
    private Restaurant restaurant;
    @Enumerated(EnumType.STRING)
    private FoodType foodtype;
    private Date creationDate;

    private double tax=0;

    private double discount = 0;

    @Transient
    public double getPriceWithDiscount() {
        double foodDiscount = this.discount;
        double restaurantDiscount = (restaurant != null) ? restaurant.getRestaurantDiscount() : 0;

        double totalDiscount = foodDiscount + restaurantDiscount;

        // Clamp total discount to 100% maximum
        totalDiscount = Math.min(totalDiscount, 100);

        double discountedPrice = price - (price * totalDiscount / 100);

        return Math.round(discountedPrice * 100.0) / 100.0; // round to 2 decimal places
    }

    @Transient
    public double getTotalDiscount() {
        double foodDiscount = this.discount;
        double restaurantDiscount = (restaurant != null) ? restaurant.getRestaurantDiscount() : 0;
        return foodDiscount + restaurantDiscount;
    }



}
