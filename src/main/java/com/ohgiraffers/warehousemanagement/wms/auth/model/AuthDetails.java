package com.ohgiraffers.warehousemanagement.wms.auth.model;

import com.ohgiraffers.warehousemanagement.wms.user.model.common.UserStatus;
import com.ohgiraffers.warehousemanagement.wms.user.model.dto.LoginUserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AuthDetails implements UserDetails {

    private LoginUserDTO loginUserDTO;

    public AuthDetails() {}

    public AuthDetails(LoginUserDTO loginUserDTO) {
        this.loginUserDTO = loginUserDTO;
    }

    public LoginUserDTO getLoginUserDTO() {
        return loginUserDTO;
    }

    public void setLoginUserDTO(LoginUserDTO loginUserDTO) {
        this.loginUserDTO = loginUserDTO;
    }

    // 권한 정보 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        String status = loginUserDTO.getUserStatus();
        String role = loginUserDTO.getUserRole();
        String part = loginUserDTO.getUserPart();

        // 기본 권한 추가
        authorities.add(new SimpleGrantedAuthority(status));
        authorities.add(new SimpleGrantedAuthority(role));
        authorities.add(new SimpleGrantedAuthority(part));

        // 재직중 상태인 경우에만 추가 권한 부여
        if (UserStatus.재직중.getStatus().equals(status)) {
            // 모든 재직중인 사용자에게 조회 권한 부여
            authorities.add(new SimpleGrantedAuthority("READ_ALL"));

            // 통합 부서는 관리자 직책만 가질 수 있음
            // 통합 부서 + 관리자인 경우 모든 도메인 접근 권한 부여
            if ("통합".equals(part) && "관리자".equals(role)) {
                authorities.add(new SimpleGrantedAuthority("통합_관리자"));
            }
            // 일반 부서의 경우 부서 + 직책 조합으로 권한 부여
            else {
                authorities.add(new SimpleGrantedAuthority(part + "_" + role));
            }
        }

        return authorities;
    }

    // 비밀번호 반환
    @Override
    public String getPassword() {
        return loginUserDTO.getUserPass();
    }

    // 이름(유저코드) 반환
    @Override
    public String getUsername() {
        return loginUserDTO.getUserCode();
    }

    // 계정 만료 여부를 표현하는 메서드
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 잠겨있는 계정을 확인하는 메서드
    // 블랙 상태인 경우 계정 잠금 처리, 승인거부는 잠금하지 않음
    @Override
    public boolean isAccountNonLocked() {
        String status = loginUserDTO.getUserStatus();
        return !UserStatus.블랙.getStatus().equals(status) && !UserStatus.퇴사.getStatus().equals(status);
    }

    // 탈퇴 계정 여부를 표현하는 메서드
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 비활성화 여부로 사용자가 사용할 수 없는 상태
    // 승인대기, 휴직중, 재직중, 승인거부 상태인 경우만 활성화
    @Override
    public boolean isEnabled() {
        String status = loginUserDTO.getUserStatus();
        return UserStatus.승인대기.getStatus().equals(status) 
            || UserStatus.휴직중.getStatus().equals(status) 
            || UserStatus.재직중.getStatus().equals(status)
            || UserStatus.승인거부.getStatus().equals(status);
    }

    // id 반환
    public Integer getUserId() {
        return loginUserDTO.getUserId();
    }

    // 이름(실제이름) 반환
    public String getUserRealName() {
        return loginUserDTO.getUserName();
    }

    // 직책 반환
    public String getUserRole() {
        return loginUserDTO.getUserRole();
    }

    // 부서 반환
    public String getUserPart() {
        return loginUserDTO.getUserPart();
    }

    // 상태 반환
    public String getUserStatus() {
        return loginUserDTO.getUserStatus();
    }
}