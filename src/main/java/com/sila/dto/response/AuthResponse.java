package com.sila.dto.response;

import com.sila.util.enums.ROLE;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String accessToken;
    private String refreshToken; // Make sure this is defined
    private Long userId;
    private ROLE role;
    private String message;
}
