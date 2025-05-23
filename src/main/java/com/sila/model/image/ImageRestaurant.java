package com.sila.model.image;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sila.model.Food;
import com.sila.model.Restaurant;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageRestaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    private String publicId;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @JsonBackReference
    @JsonIgnore
    private Restaurant restaurant;
}