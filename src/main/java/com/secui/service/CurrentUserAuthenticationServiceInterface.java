package com.secui.service;

import com.secui.entity.UserEntity;
import org.springframework.security.core.Authentication;

public interface CurrentUserAuthenticationServiceInterface {
    Authentication getAuthentication();

    String getCurrentUserName();

    UserEntity getCurrentUser();

    boolean loggedIn(boolean b);
}
