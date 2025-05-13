package com.sila.service;

import com.sila.dto.request.AddressRequest;
import com.sila.dto.response.AddressResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AddressService {
    ResponseEntity<AddressResponse> add(AddressRequest addressRequest) throws Exception;
    ResponseEntity<String> delete(Long addressId) throws Exception;
    ResponseEntity<AddressResponse> byId(Long addressId);
    ResponseEntity<List<AddressResponse>> gets();
}
