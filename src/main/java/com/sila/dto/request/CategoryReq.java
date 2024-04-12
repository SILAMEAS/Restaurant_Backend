package com.sila.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryReq {
    @NotEmpty
    private String name;
    private String key;
}
