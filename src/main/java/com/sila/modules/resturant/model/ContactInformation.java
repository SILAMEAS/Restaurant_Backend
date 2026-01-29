package com.sila.modules.resturant.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor // Required by JPA
@AllArgsConstructor
@Builder
public class ContactInformation {
    @NotEmpty
    private String email;

    @NotEmpty
    private String phone;

    private String twitter;
    private String instagram;
}

