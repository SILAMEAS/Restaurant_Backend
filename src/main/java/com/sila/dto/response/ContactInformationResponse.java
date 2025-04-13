package com.sila.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactInformationResponse {
    private String phone;
    private String email;
}
