package com.sila.dto.response;

import com.sila.model.User;
import com.sila.util.enums.ROLE;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserResponseCustom {
        private Long id;
        private String profile;
        private String fullName;
        private String email;
        private ROLE role;
        private int orders;
        private LocalDateTime createdAt;
    }

    public static UserResponseCustom toUserResponseCustom(User user) {
        return UserResponseCustom.builder()
                .id(user.getId())
                .orders(user.getOrders().size())
                .email(user.getEmail())
                .profile(user.getProfile())
                .fullName(user.getFullName())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}

