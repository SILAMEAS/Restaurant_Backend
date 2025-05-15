package com.sila.controller;

import com.sila.dto.request.AddressRequest;
import com.sila.dto.response.AddressResponse;
import com.sila.service.AddressService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
            ) throws Exception {
        return addressService.add(addressRequest);
    }
    @PutMapping("/{id}")
    ResponseEntity<List<AddressResponse>> updateAddress(
            @ModelAttribute @Valid AddressRequest addressRequest,
            @PathVariable Long id
    ) throws Exception {
        return addressService.update(addressRequest,id);
    }
    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteAddress(
            @PathVariable Long id
    ) throws Exception {
        return addressService.delete(id);
    }
    @GetMapping()
    ResponseEntity<List<AddressResponse>> getAddresses(

    ) {
        return addressService.gets();
    }
    @GetMapping("/{id}")
    ResponseEntity<AddressResponse> getAddressById(@PathVariable Long id

    ) {
        return addressService.byId(id);
    }
}
