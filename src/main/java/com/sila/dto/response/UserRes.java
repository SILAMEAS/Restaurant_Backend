package com.sila.dto.response;

import com.sila.model.Address;
import com.sila.utlis.enums.USER_ROLE;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class UserRes implements Serializable {
    private String profile;
    private String fullName;
    private String email;
    private USER_ROLE role;
    private List<Address> addresses;
}
