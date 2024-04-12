package com.sila.config;

import com.sila.exception.NotFoundException;
import com.sila.utlis.enums.USER_ROLE;
import com.sila.model.User;
import com.sila.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
  public class CustomerUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email);
    if(user==null) {
      throw new NotFoundException("invalid email");
    }
    USER_ROLE role= user.getRole();
    if(role==null)role=USER_ROLE.ROLE_CUSTOMER;
    List<GrantedAuthority> authorise=new ArrayList<>();
    authorise.add(new SimpleGrantedAuthority(role.toString()));
    return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),authorise);
  }
}
