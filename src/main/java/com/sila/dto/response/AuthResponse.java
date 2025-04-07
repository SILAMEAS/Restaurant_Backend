package com.sila.dto.response;

import com.sila.utlis.enums.USER_ROLE;
import lombok.Data;

@Data
public class AuthResponse {
    private String accessToken;
    private String refreshToken; // Make sure this is defined
    private Long userId;
    private USER_ROLE role;
    private String message;
}
