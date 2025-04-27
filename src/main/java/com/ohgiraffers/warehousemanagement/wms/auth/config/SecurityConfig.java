package com.ohgiraffers.warehousemanagement.wms.auth.config;

import com.ohgiraffers.warehousemanagement.wms.auth.config.handler.AuthFailHandler;
import com.ohgiraffers.warehousemanagement.wms.user.model.common.UserRole;
import com.ohgiraffers.warehousemanagement.wms.user.model.common.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthFailHandler authFailHanlder;

    @Autowired
    public SecurityConfig(AuthFailHandler authFailHanlder) {
        this.authFailHanlder = authFailHanlder;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .requestMatchers("/css/**", "/common.css", "/js/**", "/images/**", "/fonts/**", "/webjars/**");
    }

    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
            // 모든 사용자가 접근 가능한 경로
            auth.requestMatchers("/auth/login", "/user/signup", "/auth/fail", "/").permitAll();
            
            // 관리자 전용 경로
            auth.requestMatchers("/admin/**").hasAnyAuthority(UserRole.관리자.getRole());
            
            // 매니저 이상 전용 경로
            auth.requestMatchers("/manage/**").hasAnyAuthority(UserRole.매니저.getRole(), UserRole.관리자.getRole());
            
            // 일반 직원 이상 전용 경로 (재직중인 직원만)
            auth.requestMatchers("/staff/**").hasAuthority(UserStatus.재직중.getStatus());
            
            // 프로필 및 기본 기능 (재직중 또는 휴직중인 직원만)
            auth.requestMatchers("/user/profile/**").hasAnyAuthority(
                UserStatus.재직중.getStatus(), UserStatus.휴직중.getStatus(), UserStatus.승인대기.getStatus()
            );
            
            // 프로필 업데이트 (재직중 또는 휴직중인 직원만)
            auth.requestMatchers("/user/update/**", "/user/password-verify/**").hasAnyAuthority(
                UserStatus.재직중.getStatus(), UserStatus.휴직중.getStatus()
            );
            
            // 기타 모든 요청은 인증 필요
            auth.anyRequest().authenticated();
        }).formLogin(login -> {
            login.loginPage("/auth/login");
            login.usernameParameter("userCode");
            login.passwordParameter("userPass");
            login.defaultSuccessUrl("/", true);
            login.failureHandler(authFailHanlder);
        }).logout(logout -> {
            logout.logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"));
            logout.deleteCookies("JSESSIONID");
            logout.invalidateHttpSession(true);
            logout.logoutSuccessUrl("/");
        }).sessionManagement(session -> {
            session.maximumSessions(1);
            session.invalidSessionUrl("/");
        }).exceptionHandling(exception -> {
            exception.accessDeniedPage("/");  // 권한이 없을 때 메인 페이지로 리다이렉트
        }).csrf(csrf -> csrf.disable());

        return http.build();
    }
}