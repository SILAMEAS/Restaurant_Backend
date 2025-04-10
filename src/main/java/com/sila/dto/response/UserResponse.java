package com.sila.dto.response;

import com.sila.model.Address;
import com.sila.util.enums.USER_ROLE;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

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
    private USER_ROLE role;
    private List<Address> addresses;
    private List<FavoriteResponse> favourites;
}
