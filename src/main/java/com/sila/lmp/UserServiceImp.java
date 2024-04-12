package com.sila.lmp;

import com.sila.config.JwtProvider;
import com.sila.exception.BadRequestException;
import com.sila.model.User;
import com.sila.repository.UserRepository;
import com.sila.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    @Override
    public User findUserByJwtToken(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        return findUserByEmail(email);
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


}
