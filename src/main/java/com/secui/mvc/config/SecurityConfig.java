package com.secui.mvc.config;

import com.secui.mvc.service.PortalInterface;
import com.secui.mvc.serviceimpl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomLogoutSuccessHandler myLogoutSuccessHandler;

    private final UserDetailsService userDetailsService;

    private final CustomLogOutHandler customLogOutHandler;

    private final CustomSuccessHandler customSuccessHandler;

    private final CustomLoginFailureHandler customLoginFailureHandler;
    private final PortalInterface portalInterface;
    private final DataSource dataSource;
    private final ApiKeyAuthFilter apiKeyAuthFilter;



    @Bean
    public SecurityFilterChain configureSecurity(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterAfter(apiKeyAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/admin/**").authenticated()
                                .requestMatchers("/api/**").permitAll()
                                .requestMatchers("/**").permitAll())
                .exceptionHandling(exception -> exception.accessDeniedPage("/access-denied"))
                .rememberMe(rememberMe -> rememberMe
                        .rememberMeCookieName("remember-me")
                        .rememberMeCookieDomain("domain")
                        .tokenRepository(persistentTokenRepository())
                        .tokenValiditySeconds(24 * 60 * 60)
                        .key("uniqueAndSecret")
                        .userDetailsService(userDetailsService()))
                .formLogin(formLogin -> formLogin.loginPage("/login")
                        .usernameParameter("userName")
                        .passwordParameter("password")
                        .successHandler(customSuccessHandler)
                        .failureHandler(customLoginFailureHandler)
                        .failureUrl("/login?error=true"))
                .logout(log -> log.deleteCookies("JSESSIONID")
                        .addLogoutHandler(customLogOutHandler)
                        .logoutSuccessHandler(myLogoutSuccessHandler)
                        .invalidateHttpSession(true))
                .sessionManagement(session -> session.maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
                        .sessionRegistry(sessionRegistry())
                        .expiredUrl("/login?expired=true"))
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
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;
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

    @Bean
    public FilterRegistrationBean<CORSFilter> corsFilterRegister() {
        FilterRegistrationBean<CORSFilter> bean = new FilterRegistrationBean<>(new CORSFilter(portalInterface));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
