package com.sila.dto.response;

import lombok.*;

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
