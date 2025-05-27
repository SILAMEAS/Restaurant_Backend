package com.sila.dto.request;

import com.sila.dto.method.OnCreate;
import com.sila.model.Address;
import com.sila.model.ContactInformation;
import jakarta.validation.Valid;
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

    @Valid
    @NotNull(groups = OnCreate.class,message = "address is required during create")
    private Address address;

    @Valid
    @NotNull(groups = OnCreate.class,message = "contactInformation is required")
    private ContactInformation contactInformation;

    @NotEmpty(message = "openingHours is required")
    private String openingHours;

    @NotEmpty(groups = OnCreate.class, message = "images is required")
    private List<MultipartFile> images;

    @NotNull
    private Boolean open; // use Boolean to allow null-check validation
}
