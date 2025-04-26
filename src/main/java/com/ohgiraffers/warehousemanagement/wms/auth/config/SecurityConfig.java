package com.ohgiraffers.warehousemanagement.wms.auth.config;

import com.ohgiraffers.warehousemanagement.wms.auth.config.handler.AuthFailHandler;
import com.ohgiraffers.warehousemanagement.wms.user.model.common.UserRole;
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
            auth.requestMatchers("/auth/login", "/user/signup", "/auth/fail", "/").permitAll();
            auth.requestMatchers("/admin/**").hasAnyAuthority(UserRole.관리자.getRole());
            auth.requestMatchers("/manage/**").hasAnyAuthority(UserRole.매니저.getRole());
            auth.requestMatchers("/staff/**").hasAnyAuthority(UserRole.사원.getRole());
            // 사용자 프로필 업데이트는 로그인한 사용자만 가능하도록 설정
            auth.requestMatchers("/user/update/**", "/user/password-verify/**", "/user/profile/**").authenticated();
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
