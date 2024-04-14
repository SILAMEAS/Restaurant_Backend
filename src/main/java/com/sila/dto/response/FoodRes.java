package com.sila.dto.response;

import com.sila.model.Category;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class FoodRes implements Serializable {
  private Long id;
  @NotEmpty
  private String name;
  @NotEmpty
  private Long categoryId;
  @NotEmpty
  private String description;
  @NotEmpty
  private Long price;
  @NotEmpty
  private List<String> images;
  @NotEmpty
  private Long restaurantId;
  @NotEmpty
  private Category foodCategory;
  private boolean isVegetarian;
  private boolean isSeasonal;
  private boolean available;
}
