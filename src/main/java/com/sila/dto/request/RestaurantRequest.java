package com.sila.dto.request;

import com.sila.model.Address;
import com.sila.model.ContactInformation;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;

@Data
public class RestaurantRequest {
  private Long id;
  @NotEmpty
  private String name;
  @NotEmpty
  private String description;
  private String cuisineType;
  private Address address;
  private ContactInformation contactInformation;
  @NotEmpty
  private String openingHours;
  @NotEmpty
  private List<String> images;
  private boolean open;
}

