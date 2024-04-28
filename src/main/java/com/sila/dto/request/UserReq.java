package com.sila.dto.request;

import com.sila.model.Address;
import com.sila.utlis.enums.USER_ROLE;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserReq {
    private String profile;
    private String fullName;
    private List<Address> addresses;
}
