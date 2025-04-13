package com.sila.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactInformation {
    @NotEmpty
    private String email;

    @NotEmpty
    private String phone;

    private String twitter;
    private String instagram;
}

