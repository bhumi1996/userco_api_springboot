package com.secui.config;

import com.secui.entity.UserEntity;
import com.secui.service.UserInterface;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final UserInterface userInterface;

    @Value("${MAX_FAILED_ATTEMPTS}")
    private int maxFailedAttempt;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String email = request.getParameter("email");
        UserEntity userEntity = userInterface.findByEmail(email);
        if (userEntity != null) {
            if (userEntity.isEnabled() && userEntity.isAccountLocked()) {
                if (userEntity.getFailedAttempt() < maxFailedAttempt - 1) {
                    userInterface.increaseFailedAttempts(userEntity);
                } else {
                    userInterface.lock(userEntity);
                    exception = new LockedException("Your account has been locked due to 3 failed attempts.It will be unlocked after 24 hours.");
                }
            } else if (!userEntity.isAccountLocked()) {
                if (userInterface.unlockWhenTimeExpired(userEntity)) {
                    exception = new LockedException("Your account has been unlocked. Please try to login again.");
                }
            }

        }
        super.setDefaultFailureUrl("/login?error=true");
        super.onAuthenticationFailure(request, response, exception);
    }

}
