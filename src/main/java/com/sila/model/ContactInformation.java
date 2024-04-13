package com.sila.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactInformation {
    private  String email;
    private String phone;
    private String twitter;
    private String instagram;

}
