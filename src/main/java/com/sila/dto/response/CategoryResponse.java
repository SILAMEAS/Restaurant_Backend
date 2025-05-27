package com.sila.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class CategoryResponse {
    private Long id;
    private String name;
    private String url;
    private String publicId;
    private String restaurant;
    private int items;
}
