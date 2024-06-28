package com.secui.config;


import com.secui.entity.UserEntity;
import com.secui.service.CurrentUserAuthenticationServiceInterface;
import com.secui.service.UserInterface;
import com.secui.serviceimpl.UserDetailsImpl;
import com.secui.utility.UrlConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final UserInterface userInterface;
    private final CurrentUserAuthenticationServiceInterface currentUserAuthenticationServiceInterface;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UserEntity userEntity = userInterface.findByEmail(userDetails.getUsername());
        if (userEntity.getFailedAttempt()!=null && userEntity.getFailedAttempt() > 0) {
            userInterface.resetFailedAttempts(userEntity.getEmail());
        }
        String targetUrl = UrlConstants.ADMIN_DASHBOARD_URL;
        currentUserAuthenticationServiceInterface.loggedIn(true);
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }
}
