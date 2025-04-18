package com.sila.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {
    private String streetAddress;
    private String city;
    private String country;
    private String stateProvince;
    private String postalCode;
}