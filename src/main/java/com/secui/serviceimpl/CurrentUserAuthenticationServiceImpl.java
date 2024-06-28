package com.secui.serviceimpl;

import com.secui.entity.UserEntity;
import com.secui.service.CurrentUserAuthenticationServiceInterface;
import com.secui.service.UserInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserAuthenticationServiceImpl implements CurrentUserAuthenticationServiceInterface {
    private final UserInterface userInterface;
    @Override
    public Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return authentication;
    }

    @Override
    public String getCurrentUserName() {
        return userInterface.findByEmail(getAuthentication().getName()).getFullName();
    }

    @Override
    public UserEntity getCurrentUser() {
        return userInterface.findByEmail(getAuthentication().getName());
    }

    @Override
    public boolean loggedIn(boolean loggedIn) {
        UserEntity userModel = getCurrentUser();
        userModel.setLogged(loggedIn);
        userModel.setLastModifiedBy(getCurrentUserName());
        return userInterface.update(userModel);
    }
}
