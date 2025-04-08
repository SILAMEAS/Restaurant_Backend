package com.sila.service.lmp;

import com.sila.config.JwtProvider;
import com.sila.dto.entityResponseHandler.EntityResponseHandler;
import com.sila.dto.request.UserRequest;
import com.sila.dto.response.FavoriteResponse;
import com.sila.dto.response.UserResponse;
import com.sila.exception.BadRequestException;
import com.sila.exception.NotFoundException;
import com.sila.model.User;
import com.sila.repository.UserRepository;
import com.sila.service.UserService;
import com.sila.specifcation.UserSpecification;
import com.sila.utlis.context.UserContext;
import com.sila.utlis.enums.USER_ROLE;
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

    @Override
    public User findUserByJwtToken(String jwt) {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        return findUserByEmail(email);
    }

    @Override
    public Boolean findUserHasRoleAdmin(String jwt) {
        var user = findUserByJwtToken(jwt);
        return user.getRole().equals(USER_ROLE.ROLE_ADMIN);
    }

    @Override
    public User findUserByEmail(String email) {
        User foundUser = userRepository.findByEmail(email);
        if (foundUser == null) {
            throw new NotFoundException("User not found");
        }
        return foundUser;
    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new BadRequestException("User not found"));
    }

    @Override
    public EntityResponseHandler<UserResponse> listUser(Pageable pageable, String search) {
        Specification<User> spec = Specification.where(null);
        if (search != null) {
            spec = spec.and(UserSpecification.search(search));
        }
        return new EntityResponseHandler<>(userRepository.findAll(spec, pageable).map(re -> this.modelMapper.map(re, UserResponse.class)));
    }

    @Override
    public UserResponse updateProfile(User user, UserRequest userReq) {
        if (!userReq.getProfile().isEmpty()) {
            user.setProfile(userReq.getProfile());

        }
        if(!userReq.getAddresses().isEmpty()){
            user.setAddresses(userReq.getAddresses());
        }
        if (!userReq.getFullName().isEmpty()) {
            user.setFullName(userReq.getFullName());
        }
        return this.modelMapper.map(userRepository.save(user), UserResponse.class);
    }

    @Transactional
    @Override
    public UserResponse getUserProfile() throws Exception {
        User userFromContext = UserContext.getUser(); // JWT-based context
        User user = userRepository.findByIdWithFavorites(userFromContext.getId())
                .orElseThrow(() -> new Exception("User not found"));

        UserResponse userRes = this.modelMapper.map(user, UserResponse.class);
        userRes.setFavourites(user.getFavourites().stream()
                .map(fav -> new FavoriteResponse(fav.getId(), fav.getName(), fav.getDescription(), user.getId(), fav.getRestaurant().getId()))
                .toList());

        return userRes;
    }


}
