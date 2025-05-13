package com.sila.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "streetAddress can't empty")
    private String streetAddress;
    @NotEmpty(message = "name can't empty")
    private String name;
    @NotEmpty(message = "city can't empty")
    private String city;
    @NotEmpty(message = "stateProvince can't empty")
    private String stateProvince;
    @NotEmpty(message = "postalCode can't empty")
    private String postalCode;
    @NotEmpty(message = "country can't empty")
    private String country;
    @JoinColumn(name ="user_id",referencedColumnName = "id",foreignKey = @ForeignKey(name = "fk_user_address"))
    @ManyToOne
    private User user;

}
