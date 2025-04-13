package com.sila.dto.request;

import com.sila.model.Address;
import com.sila.model.ContactInformation;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class RestaurantRequest {
    @NotNull(message = "name is required")
    private String name;
    @NotNull(message = "description is required")
    private String description;
    private String cuisineType;
    @NotNull(message = "address is required")
    private Address address;
    private ContactInformation contactInformation;
    @NotNull(message = "openingHours is required")
    private String openingHours;
    @NotNull(message = "images is required")
    private List<MultipartFile> images;
    @NotNull(message = "open is required")
    private boolean open;

}

