package com.sila.dto.request;

import com.sila.util.enums.ROLE;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserRequest {

    @NotBlank
    private String fullName;

    @NotNull
    private ROLE role;


}
