package com.sila.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private  Long id;
  @NotEmpty(message ="streetAddress can't empty" )
  private String streetAddress;
  private String city;
  private String stateProvince;
  private String postalCode;
  private String country;
  @ManyToOne
  private User user;

}
