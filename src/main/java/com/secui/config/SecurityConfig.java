package com.secui.config;

import com.secui.serviceimpl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomLogoutSuccessHandler myLogoutSuccessHandler;

    private final UserDetailsService userDetailsService;

    private final CustomLogOutHandler customLogOutHandler;

    private final CustomSuccessHandler customSuccessHandler;

    private final CustomLoginFailureHandler customLoginFailureHandler;



    @Bean
    public SecurityFilterChain configureSecurity(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/admin/**").authenticated().requestMatchers("/api/**").permitAll().requestMatchers("/**").permitAll())
                .exceptionHandling(exception -> exception.accessDeniedPage("/access-denied"))
                .rememberMe(rememberMe -> rememberMe.userDetailsService(userDetailsService()))
                .formLogin(formLogin -> formLogin.loginPage("/login").usernameParameter("userName").passwordParameter("password").successHandler(customSuccessHandler).failureHandler(customLoginFailureHandler).failureUrl("/login?error=true"))
                .logout(log -> log.deleteCookies("JSESSIONID").addLogoutHandler(customLogOutHandler).logoutSuccessHandler(myLogoutSuccessHandler).invalidateHttpSession(true))
                .sessionManagement(session -> session.maximumSessions(1).maxSessionsPreventsLogin(true).sessionRegistry(sessionRegistry()).expiredUrl("/login?expired=true"))
                .build();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
    @Bean
    public UserDetailsService userDetailsService() {
        return  new UserDetailsServiceImpl() ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }


}
