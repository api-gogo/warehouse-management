package com.ohgiraffers.warehousemanagement.wms.auth.config;

import com.ohgiraffers.warehousemanagement.wms.auth.config.handler.AuthFailHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

    private final AuthFailHandler authFailHandler;

    @Autowired
    public SecurityConfig(AuthFailHandler authFailHandler) {
        this.authFailHandler = authFailHandler;
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

            // 2. 통합 부서 관리자는 유저 관리에 접근 가능
            auth.requestMatchers("/admin/**").hasAuthority("통합_관리자");

            // 3. 프로필 페이지 접근 권한 (모든 상태 사용자)
            auth.requestMatchers(HttpMethod.GET, "/user/profile").hasAnyAuthority(
                    "재직중", "휴직중", "승인대기", "승인거부"
            );

            // 4. 프로필 업데이트 관련 페이지 접근 권한 (승인대기 제외 모든 사용자)
            auth.requestMatchers(HttpMethod.PATCH, "/user/profile").hasAnyAuthority(
                    "재직중", "휴직중", "승인거부"
            );

            auth.requestMatchers("/user/password-verify").hasAnyAuthority(
                    "재직중", "휴직중", "승인거부"
            );

            // ==================== 업무 도메인별 접근 권한 ==================== //

            // 5. 전체 조회, 상세 조회 - 재직중인 사원은 모두 볼 수 있음.
            auth.requestMatchers(HttpMethod.GET, "/storages", "/storages/{id:\\d+}").hasAnyAuthority("재직중");
            auth.requestMatchers(HttpMethod.GET, "/shipments", "/shipments/{id:\\d+}").hasAnyAuthority("재직중");
            auth.requestMatchers(HttpMethod.GET, "/inventories", "/inventories/{id:\\d+}").hasAnyAuthority("재직중");
            auth.requestMatchers(HttpMethod.GET, "/inspections", "/inspections/{id:\\d+}").hasAnyAuthority("재직중");
            auth.requestMatchers(HttpMethod.GET, "/returns", "/returns/{id:\\d+}").hasAnyAuthority("재직중");
            auth.requestMatchers(HttpMethod.GET, "/purchases", "/purchases/{id:\\d+}").hasAnyAuthority("재직중");
            auth.requestMatchers(HttpMethod.GET, "/sales", "/sales/{id:\\d+}").hasAnyAuthority("재직중");
            auth.requestMatchers(HttpMethod.GET, "/products", "/products/{id:\\d+}").hasAnyAuthority("재직중");
            auth.requestMatchers(HttpMethod.GET, "/suppliers", "/suppliers/{id:\\d+}").hasAnyAuthority("재직중");
            auth.requestMatchers(HttpMethod.GET, "/stores", "/stores/{id:\\d+}").hasAnyAuthority("재직중");

            // 6. 각 부서별 작업에 대한 권한 설정 -> 이후 직책별에 맞게 수정 필요
            // 입고 부서
            auth.requestMatchers("/storages/**").hasAnyAuthority("입고_사원", "입고_매니저", "입고_관리자", "통합_관리자");
            // 출고 부서
            auth.requestMatchers("/shipments/**").hasAnyAuthority("출고_사원", "출고_매니저", "출고_관리자", "통합_관리자");
            // 재고 부서
            auth.requestMatchers("/inventories/**").hasAnyAuthority("재고_사원", "재고_매니저", "재고_관리자", "통합_관리자");
            // 검수 부서
            auth.requestMatchers("/inspections/**").hasAnyAuthority("검수_사원", "검수_매니저", "검수_관리자", "통합_관리자");
            // 반품 부서
            auth.requestMatchers("/returns/**").hasAnyAuthority("반품_사원", "반품_매니저", "반품_관리자", "통합_관리자");
            // 발주 부서
            auth.requestMatchers("/purchases/delete/{id:\\d+}", "/purchases/complete/{id:\\d+}").hasAnyAuthority("발주_매니저", "발주_관리자", "통합_관리자");
            auth.requestMatchers("/purchases/**").hasAnyAuthority("반품_사원", "반품_매니저", "반품_관리자", "통합_관리자");
            // 수주 부서
            auth.requestMatchers("/sales/**", "/stores/**").hasAnyAuthority("수주_사원", "수주_매니저", "수주_관리자", "통합_관리자");
            // 상품 부서
            auth.requestMatchers("/products/**").hasAnyAuthority("상품_사원", "상품_매니저", "상품_관리자", "통합_관리자");

            // 7. 기타 모든 요청은 인증 필요
            auth.anyRequest().authenticated();
        }).formLogin(login -> {
            login.loginPage("/auth/login");
            login.usernameParameter("userCode");
            login.passwordParameter("userPass");
            login.defaultSuccessUrl("/", true);
            login.failureHandler(authFailHandler);
        }).logout(logout -> {
            logout.logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"));
            logout.deleteCookies("JSESSIONID");
            logout.invalidateHttpSession(true);
            logout.logoutSuccessUrl("/");
        }).sessionManagement(session -> {
            session.maximumSessions(1);
            session.invalidSessionUrl("/");
        }).exceptionHandling(exception -> {
            exception.accessDeniedHandler((request, response, accessDeniedException) -> {
                // 이전 페이지 URL 가져오기
                String referer = request.getHeader("Referer");
                // 기본 리다이렉트 URL (Referer 없을 경우 대비)
                String targetUrl = "/";

                // Referer 값이 있고, 현재 요청 경로와 다른 경우 사용 (무한 리다이렉트 방지)
                String requestedUri = request.getRequestURI();
                if (referer != null && !referer.isEmpty() && !referer.contains(requestedUri)) {
                    targetUrl = referer;
                }

                // 리다이렉트 URL에 간단한 플래그 추가
                String redirectUrl;
                if (targetUrl.contains("?")) {
                    redirectUrl = targetUrl + "&accessDenied=true";
                } else {
                    redirectUrl = targetUrl + "?accessDenied=true";
                }

                response.sendRedirect(redirectUrl); // 최종 URL로 리다이렉트
            });
        }).csrf(csrf -> csrf.disable());

        return http.build();
    }
}