package com.sila.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sila.utlis.enums.USER_ROLE;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String fullName;
  private String profile;
  private String email;
  private String password;
  private USER_ROLE role=USER_ROLE.ROLE_CUSTOMER;
//  @JsonIgnore
//  @OneToMany(cascade = CascadeType.ALL,mappedBy ="customer")
//  private List<Order> orders=new ArrayList<>();
//  @ElementCollection
//  private List<RestaurantFavRes> favourites=new ArrayList<>();
  @JsonIgnore
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "owner")
  private List<Favorite> favourites = new ArrayList<>();
  @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "user")
   private  List<Address> addresses=new ArrayList<>();
}
