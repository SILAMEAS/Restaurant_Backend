package com.sila.modules.profile.dto.req;

import com.sila.modules.address.model.Address;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserRequest {
    private String profile;
    private String fullName;
    private List<Address> addresses;
}
