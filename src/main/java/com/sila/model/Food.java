package com.sila.model;

import jakarta.persistence.*;
import lombok.*;

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
    private Long price;
    @ManyToOne
    private Category category;
    @Column(length = 1000)
    private List<String> images;
    private boolean available;
    @ManyToOne
    private Restaurant restaurant;
    private boolean isVegetarian;
    private boolean isSeasonal;
    private Date creationDate;

}
