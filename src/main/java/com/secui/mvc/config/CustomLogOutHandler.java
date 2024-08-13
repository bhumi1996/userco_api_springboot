package com.secui.mvc.config;

import com.secui.mvc.entity.UserEntity;
import com.secui.mvc.service.CurrentUserAuthenticationServiceInterface;
import com.secui.mvc.service.UserInterface;
import com.secui.mvc.serviceimpl.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomLogOutHandler implements LogoutHandler {
    private final CurrentUserAuthenticationServiceInterface currentUserAuthenticationServiceInterface;
    private final UserInterface userInterface;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            UserEntity userEntity = userInterface.findByEmail(userDetails.getUsername());
            currentUserAuthenticationServiceInterface.loggedIn(false);
        }
    }
}
