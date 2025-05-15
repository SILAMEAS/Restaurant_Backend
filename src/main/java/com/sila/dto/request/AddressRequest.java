package com.sila.dto.request;

import com.sila.dto.method.OnCreate;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequest {
    @NotEmpty(groups = OnCreate.class,message = "name is required")
    private String name;
    @NotEmpty(groups = OnCreate.class,message = "stressAddress is required")
    private String streetAddress;
    @NotEmpty(groups = OnCreate.class,message = "city is required")
    private String city;
    @NotEmpty(groups = OnCreate.class,message = "stateProvince is required")
    private String stateProvince;
    @NotEmpty(groups = OnCreate.class,message = "postalCode is required")
    private String postalCode;
    @NotEmpty(groups = OnCreate.class,message = "country is required")
    private String country;

    private Boolean currentUsage = false;
}
