package com.sila.service.lmp;

import com.sila.config.context.UserContext;
import com.sila.dto.request.AddressRequest;
import com.sila.dto.response.AddressResponse;
import com.sila.model.Address;
import com.sila.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressImp implements AddressService {
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<AddressResponse> add(AddressRequest req) {
        var address = Address.builder()
                .streetAddress(req.getStreetAddress())
                .city(req.getCity())
                .country(req.getCountry())
                .postalCode(req.getPostalCode())
                .user(UserContext.getUser())
                .build();

        return new ResponseEntity<>(modelMapper.map(address, AddressResponse.class), HttpStatus.CREATED);
    }
}
