package com.sila.service.lmp;

import com.sila.config.context.UserContext;
import com.sila.dto.request.AddressRequest;
import com.sila.dto.response.AddressResponse;
import com.sila.exception.BadRequestException;
import com.sila.model.Address;
import com.sila.model.Favorite;
import com.sila.model.Restaurant;
import com.sila.repository.AddressRepository;
import com.sila.repository.RestaurantRepository;
import com.sila.repository.UserRepository;
import com.sila.service.AddressService;
import com.sila.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressImp implements AddressService {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    public ResponseEntity<AddressResponse> add(AddressRequest req) throws Exception {
        var address = Address.builder()
                .streetAddress(req.getStreetAddress())
                .city(req.getCity())
                .country(req.getCountry())
                .postalCode(req.getPostalCode())
                .stateProvince(req.getStateProvince())
                .user(UserContext.getUser())
                .name(req.getName())
                .currentUsage(Boolean.TRUE.equals(req.getCurrentUsage()))
                .build();

            addressRepository.save(address);
        return new ResponseEntity<>(modelMapper.map(address, AddressResponse.class), HttpStatus.CREATED);
    }

    @Override
    @Transactional
    public ResponseEntity<String> delete(Long addressId) {
        var user = userRepository.findById(UserContext.getUser().getId())
                .orElseThrow(() -> new BadRequestException("User not found"));

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new BadRequestException("Address not found"));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("You are not authorized to delete this address");
        }

        // Detach from restaurant if necessary
        List<Restaurant> restaurants = restaurantRepository.findAllByAddressId(addressId);
        for (Restaurant restaurant : restaurants) {
            restaurant.setAddress(null);
        }
        restaurantRepository.saveAll(restaurants);

        // Detach from user
        user.getAddresses().removeIf(a -> a.getId().equals(addressId));
        userRepository.save(user);

        // Delete address
        addressRepository.delete(address);

        return new ResponseEntity<>("Delete address successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AddressResponse> byId(Long addressId) {
        return new ResponseEntity<>(findByIdWithException(addressId),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<AddressResponse>> gets() {
        List<AddressResponse> addressRes = addressRepository.findAll()
                .stream()
                .map(address -> modelMapper.map(address, AddressResponse.class)).toList();

        return new ResponseEntity<>(addressRes, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<AddressResponse>> update(AddressRequest addressRequest,Long addressId) throws Exception {
        final var user = userRepository.findById(UserContext.getUser().getId()).orElseThrow(() -> new BadRequestException("User not found"));
        addressRepository.updateAddressCurrentUsageMisMatch(user.getId(), false);
        addressRepository.updateAddressCurrentUsageMatch(addressId, true);
        return new ResponseEntity<>(user.getAddresses().stream().map(a->this.modelMapper.map(a,AddressResponse.class)).toList(),HttpStatus.OK);
    }

    private AddressResponse findByIdWithException(Long addressId){
        Optional<Address> address = addressRepository.findById(addressId);
        if(address.isEmpty()){
            throw new BadRequestException("Address not found");
        }
        return modelMapper.map(address,AddressResponse.class);
    }
}
