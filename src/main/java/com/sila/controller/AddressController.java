package com.sila.controller;

import com.sila.dto.request.AddressRequest;
import com.sila.dto.response.AddressResponse;
import com.sila.service.AddressService;
import com.sila.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Address Controller", description = "Address operations related to Cart")
@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
@Slf4j
public class AddressController {
    private final AddressService addressService;
    @PostMapping()
    ResponseEntity<AddressResponse> addAddress(
            @ModelAttribute @Valid AddressRequest addressRequest
            ) {
        return addressService.add(addressRequest);
    }
}
