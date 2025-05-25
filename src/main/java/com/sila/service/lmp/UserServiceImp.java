package com.sila.service.lmp;

import com.sila.config.jwt.JwtProvider;
import com.sila.dto.EntityResponseHandler;
import com.sila.dto.request.UserRequest;
import com.sila.dto.response.FavoriteResponse;
import com.sila.dto.response.UserResponse;
import com.sila.exception.BadRequestException;
import com.sila.exception.NotFoundException;
import com.sila.model.User;
import com.sila.repository.RestaurantRepository;
import com.sila.repository.UserRepository;
import com.sila.service.RestaurantService;
import com.sila.service.UserService;
import com.sila.specifcation.UserSpecification;
import com.sila.config.context.UserContext;
import com.sila.util.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final ModelMapper modelMapper;
    private final RestaurantService restaurantService;
    private final RestaurantRepository restaurantRepository;

    @Override
    public User getByJwt(String jwt) {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        return getByEmail(email);
    }


    @Override
    public User getByEmail(String email) {
        User foundUser = userRepository.findByEmail(email);
        if (foundUser == null) {
            throw new NotFoundException("User not found");
        }
        return foundUser;
    }

    @Override
    public User getById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new BadRequestException("User not found"));
    }

    @Override
    public EntityResponseHandler<UserResponse> list(Pageable pageable, String search) {
        Specification<User> spec = Specification.where(null);
        if (search != null) {
            spec = spec.and(UserSpecification.search(search));
        }
        return new EntityResponseHandler<>(userRepository.findAll(spec, pageable).map(re -> this.modelMapper.map(re, UserResponse.class)));
    }
    @Override
    public EntityResponseHandler<UserResponse> getUsersWhoOrderedFromRestaurant(Long restaurantId, Pageable pageable) {
        Specification<User> spec = UserSpecification.hasOrderedFromRestaurant(restaurantId);
        return new EntityResponseHandler<>(userRepository.findAll(spec, pageable).map(re -> this.modelMapper.map(re, UserResponse.class)));
    }

    @Override
    public UserResponse update( UserRequest userReq) {
        var user = UserContext.getUser();

        Utils.setIfNotNull(userReq.getProfile(), user::setProfile);
        Utils.setIfNotNull(userReq.getAddresses(), user::setAddresses);
        Utils.setIfNotNull(userReq.getFullName(), user::setFullName);

//        if (!userReq.getProfile().isEmpty()) {
//            user.setProfile(userReq.getProfile());
//
//        }
//        if(!userReq.getAddresses().isEmpty()){
//            user.setAddresses(userReq.getAddresses());
//        }
//        if (!userReq.getFullName().isEmpty()) {
//            user.setFullName(userReq.getFullName());
//        }
        return this.modelMapper.map(userRepository.save(user), UserResponse.class);
    }

    @Transactional
    @Override
    public UserResponse getProfile() throws Exception {
        User userFromContext = UserContext.getUser(); // JWT-based context
        User user = userRepository.findByIdWithFavorites(userFromContext.getId())
                .orElseThrow(() -> new Exception("User not found"));

        UserResponse userRes = this.modelMapper.map(user, UserResponse.class);
        userRes.setFavourites(user.getFavourites().stream()
                .map(fav -> new FavoriteResponse(fav.getId(), fav.getName(), fav.getDescription(), user.getId(), fav.getRestaurant().getId()))
                .toList());
        /** I try to currentUsage from null to false */
        userRes.getAddresses().forEach(address -> {
            if (address.getCurrentUsage() == null) {
                address.setCurrentUsage(false);
            }
        });
        return userRes;
    }

    @Override
    public Long all() {
        return userRepository.count();
    }




}
