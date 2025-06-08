package com.sila.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantResponse{
    private Long id;
    private String name;
    private String description;
    private String cuisineType;
    private boolean open;
    private String openingHours;
    private LocalDateTime registrationDate;
    private AddressResponse address;
    private ContactInformationResponse contactInformation;
    private List<ImageDetailsResponse> imageUrls;
    private int rating;
    private String ownerName;


    private double deliveryFee;

    private double discount;
}