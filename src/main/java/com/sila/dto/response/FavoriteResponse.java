package com.sila.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    private Long userId;
    private Long restaurantId;
}
