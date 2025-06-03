package com.sila.dto.response;

import com.sila.model.Favorite;
import com.sila.model.image.ImageRestaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteResponse {
    private Long id;
    private String name;
    private String description;
    private Long restaurantId;
    private List<ImageRestaurant> images;

    public static FavoriteResponse toResponse(Favorite favorite){

        return FavoriteResponse.builder()
                .id(favorite.getId())
                .name(favorite.getName())
                .description(favorite.getDescription())
                .images(favorite.getRestaurant().getImages())
                .restaurantId(favorite.getRestaurant().getId())
                .build();

    }
}
