package com.ohgiraffers.warehousemanagement.wms.auth.model;

import com.ohgiraffers.warehousemanagement.wms.user.model.dto.LoginUserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

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
        return Collections.singleton(new SimpleGrantedAuthority(loginUserDTO.getUserRole()));
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
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 탈퇴 계정 여부를 표현하는 메서드
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 비활성화 여부로 사용자가 사용할 수 없는 상태
    @Override
    public boolean isEnabled() {
        return true;
    }
}
