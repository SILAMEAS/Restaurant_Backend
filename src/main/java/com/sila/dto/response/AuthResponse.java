package com.sila.dto.response;

import com.sila.util.enums.USER_ROLE;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String accessToken;
    private String refreshToken; // Make sure this is defined
    private Long userId;
    private USER_ROLE role;
    private String message;
}
