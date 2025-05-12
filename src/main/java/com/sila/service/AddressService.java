package com.sila.service;

import com.sila.dto.request.AddressRequest;
import com.sila.dto.response.AddressResponse;
import org.springframework.http.ResponseEntity;

public interface AddressService {
    ResponseEntity<AddressResponse> add(AddressRequest addressRequest);
}
