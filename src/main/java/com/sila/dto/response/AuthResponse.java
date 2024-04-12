package com.sila.dto.response;

import com.sila.utlis.enums.USER_ROLE;
import lombok.Data;

@Data
public class AuthResponse {
  private String jwt;
  private String message;
  private USER_ROLE role;
  private Long userId;
}
