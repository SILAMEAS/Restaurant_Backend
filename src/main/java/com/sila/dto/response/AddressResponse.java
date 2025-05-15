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
    private String name;
    private Long id;
    private String street;
    private String city;
    private String country;
    private String state;
    private String zip;
    private Boolean currentUsage;
}