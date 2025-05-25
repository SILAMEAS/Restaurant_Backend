package com.sila.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class CategoryResponse implements Serializable {
    private Long id;
    private String name;
    private String url;
}
