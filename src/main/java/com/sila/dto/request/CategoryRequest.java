package com.sila.dto.request;

import com.sila.dto.method.OnCreate;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    @NotEmpty
    private String name;
    @NotNull(groups = OnCreate.class, message = "images is required")
    private MultipartFile image;

    private Boolean removeBg=false;

}
