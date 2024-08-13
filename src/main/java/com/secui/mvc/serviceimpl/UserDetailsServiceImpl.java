package com.secui.mvc.serviceimpl;

import com.secui.mvc.entity.UserEntity;
import com.secui.mvc.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private  UserRepository userRepository;

    public UserDetailsServiceImpl(){

    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);
        if(userEntity!=null && userEntity.isEnabled() ){
            return new UserDetailsImpl(userEntity);
        }
        throw new UsernameNotFoundException("User not Found " + username);
    }
}
