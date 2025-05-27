package com.sila.dto.response;

import com.sila.util.enums.ROLE;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserResponse implements Serializable {
    private Long id;
    private String profile;
    private String fullName;
    private String email;
    private ROLE role;
}
