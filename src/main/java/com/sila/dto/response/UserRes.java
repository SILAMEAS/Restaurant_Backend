package com.sila.dto.response;

import com.sila.model.Address;
import com.sila.utlis.enums.USER_ROLE;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserRes implements Serializable {
    private Long id;
    private String profile;
    private String fullName;
    private String email;
    private USER_ROLE role;
    private List<Address> addresses;
    private List<FavoriteResponse> favourites;
}
