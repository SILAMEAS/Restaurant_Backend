package com.sila.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressResponse {
    private String streetAddress;
    private String city;
    private String country;
    private String stateProvince;
    private String postalCode;
}