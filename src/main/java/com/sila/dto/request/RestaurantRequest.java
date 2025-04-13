package com.sila.dto.request;

import com.sila.model.Address;
import com.sila.model.ContactInformation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class RestaurantRequest {
    @NotEmpty(message = "name is required")
    private String name;
    @NotEmpty(message = "description is required")
    private String description;
    private String cuisineType;
//    private Address address;
//    private ContactInformation contactInformation;
    @NotEmpty(message = "openingHours is required")
    private String openingHours;
    @NotEmpty(message = "images is required")
    private List<MultipartFile> images;
    @NotNull
    private boolean open;

}

