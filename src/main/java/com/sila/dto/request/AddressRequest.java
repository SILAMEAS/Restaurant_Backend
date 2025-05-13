package com.sila.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequest {
    @NotEmpty(message = "name is required")
    private String name;
    @NotEmpty(message = "stressAddress is required")
    private String streetAddress;
    @NotEmpty(message = "city is required")
    private String city;
    @NotEmpty(message = "stateProvince is required")
    private String stateProvince;
    @NotEmpty(message = "postalCode is required")
    private String postalCode;
    @NotEmpty(message = "country is required")
    private String country;
}
