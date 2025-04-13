package com.sila.dto.response;

import com.sila.model.Address;
import com.sila.model.ContactInformation;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantResponse implements Serializable {
    private Long id;
    private String name;
    private String description;
    private String cuisineType;
    private boolean open;
    private String openingHours;
    private LocalDateTime registrationDate;
    private AddressResponse address;
    private ContactInformationResponse contactInformation;
    private List<String> imageUrls;

}