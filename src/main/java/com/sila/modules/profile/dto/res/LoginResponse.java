package com.sila.modules.profile.dto.res;

import com.sila.share.enums.ROLE;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String accessToken;
    private String refreshToken; // Make sure this is defined
    private Long userId;
    private ROLE role;
    private String message;
}
