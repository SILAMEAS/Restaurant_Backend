package com.sila.lmp;

import com.sila.config.JwtProvider;
import com.sila.dto.entityResponseHandler.EntityResponseHandler;
import com.sila.dto.request.UserReq;
import com.sila.dto.response.UserRes;
import com.sila.dto.specification.UserSpecification;
import com.sila.exception.BadRequestException;
import com.sila.model.User;
import com.sila.repository.UserRepository;
import com.sila.service.UserService;
import com.sila.utlis.enums.USER_ROLE;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final ModelMapper modelMapper;
    @Override
    public User findUserByJwtToken(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        return findUserByEmail(email);
    }

    @Override
    public Boolean findUserHasRoleAdmin(String jwt) throws Exception {
        var user = findUserByJwtToken(jwt);
        return user.getRole().equals(USER_ROLE.ROLE_ADMIN);
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User foundUser=userRepository.findByEmail(email);;
        if(foundUser==null){
            throw new BadRequestException("User not found");
        }
        return foundUser;
    }

    @Override
    public User findUserById(Long userId){
        return  userRepository.findById(userId).orElseThrow(()->new BadRequestException("User not found"));
    }

    @Override
    public EntityResponseHandler<UserRes> listUser(Pageable pageable, String search) throws Exception {
        Specification<User> spec = Specification.where(null);
        if(search!=null){
            spec=spec.and(UserSpecification.search(search));
        }
      return new EntityResponseHandler<>(userRepository.findAll(spec,pageable).map(re->this.modelMapper.map(re,UserRes.class)));
    }

    @Override
    public UserRes updateProfile(User user, UserReq userReq) throws Exception {
        if(!userReq.getProfile().isEmpty()){
            user.setProfile(userReq.getProfile());

        }
//        if(!userReq.getAddresses().isEmpty()){
//            user.setAddresses(userReq.getAddresses());
//        }
        if(!userReq.getFullName().isEmpty()){
            user.setFullName(userReq.getFullName());
        }
        return this.modelMapper.map(userRepository.save(user),UserRes.class);
    }


}
