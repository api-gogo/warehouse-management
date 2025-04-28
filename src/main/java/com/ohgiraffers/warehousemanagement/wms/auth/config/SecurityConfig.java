package com.ohgiraffers.warehousemanagement.wms.auth.config;

import com.ohgiraffers.warehousemanagement.wms.auth.config.handler.AuthFailHandler;
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
import org.springframework.web.filter.HiddenHttpMethodFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthFailHandler authFailHanlder;

    @Autowired
    public SecurityConfig(AuthFailHandler authFailHanlder) {
        this.authFailHanlder = authFailHanlder;
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
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
            // 1. 모든 사용자가 접근 가능한 경로
            auth.requestMatchers("/auth/login", "/user/signup", "/auth/fail", "/").permitAll();

            // 2. 통합 부서 관리자는 모든 경로에 접근 가능
            auth.requestMatchers("/**").hasAuthority("통합_관리자");

            // 3. 프로필 및 기본 기능 (재직중 또는 휴직중, 승인대기인 직원만)
            auth.requestMatchers("/user/profile/**").hasAnyAuthority(
                    UserStatus.재직중.getStatus(), UserStatus.휴직중.getStatus(), UserStatus.승인대기.getStatus()
            );

            // 4. 프로필 업데이트 (재직중 또는 휴직중인 직원만)
            auth.requestMatchers("/user/update/**", "/user/password-verify/**").hasAnyAuthority(
                    UserStatus.재직중.getStatus(), UserStatus.휴직중.getStatus()
            );
            
            // ==================== 업무 도메인별 접근 권한 ==================== //
            
            // 5. 전체 조회, 상세 조회 - 개발 중이므로 일시적으로 모든 사용자에게 허용
            auth.requestMatchers("/storages", "/storages/{id}").permitAll();
            auth.requestMatchers("/shipments", "/shipments/{id}").permitAll();
            auth.requestMatchers("/inventories", "/inventories/{id}").permitAll();
            auth.requestMatchers("/inspections", "/inspections/{id}").permitAll();
            auth.requestMatchers("/returns", "/returns/{id}").permitAll();
            auth.requestMatchers("/purchases", "/purchases/{id}").permitAll();
            auth.requestMatchers("/sales", "/sales/{id}").permitAll();
            auth.requestMatchers("/products", "/products/{id}").permitAll();
            
            // 6. 각 부서별 작업에 대한 권한 설정 -> 이후 직책별에 맞게 수정 필요
            // 입고 부서
            auth.requestMatchers("/storages/**").hasAnyAuthority("입고_사원", "입고_매니저", "입고_관리자");
            // 출고 부서
            auth.requestMatchers("/shipments/**").hasAnyAuthority("출고_사원", "출고_매니저", "출고_관리자");
            // 재고 부서
            auth.requestMatchers("/inventories/**").hasAnyAuthority("재고_사원", "재고_매니저", "재고_관리자");
            // 검수 부서
            auth.requestMatchers("/inspections/**").hasAnyAuthority("검수_사원", "검수_매니저", "검수_관리자");
            // 반품 부서
            auth.requestMatchers("/returns/**").hasAnyAuthority("반품_사원", "반품_매니저", "반품_관리자");
            // 발주 부서
            auth.requestMatchers("/purchases/**").hasAnyAuthority("발주_사원", "발주_매니저", "발주_관리자");
            // 수주 부서
            auth.requestMatchers("/sales/**").hasAnyAuthority("수주_사원", "수주_매니저", "수주_관리자");
            // 상품 부서
            auth.requestMatchers("/products/**").hasAnyAuthority("상품_사원", "상품_매니저", "상품_관리자");
            
            // 7. 기타 모든 요청은 인증 필요
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