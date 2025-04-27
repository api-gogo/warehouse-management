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

    // 권한 정보 반환 - UserRole과 UserStatus를 모두 권한으로 추가
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        // 기본 권한 (UserRole 기반)
        authorities.add(new SimpleGrantedAuthority(loginUserDTO.getUserRole()));
        
        // UserStatus 기반 추가 권한 (접두사 없이)
        authorities.add(new SimpleGrantedAuthority(loginUserDTO.getUserStatus()));
        
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
    // 승인거부(REJECTED)나 퇴사(RESIGNED) 상태인 경우 계정 잠금 처리
    @Override
    public boolean isAccountNonLocked() {
        String status = loginUserDTO.getUserStatus();
        return !(UserStatus.승인거부.getStatus().equals(status) || UserStatus.퇴사.getStatus().equals(status));
    }

    // 탈퇴 계정 여부를 표현하는 메서드
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 비활성화 여부로 사용자가 사용할 수 없는 상태
    // 승인대기, 휴직중, 재직중 상태인 경우만 활성화
    @Override
    public boolean isEnabled() {
        String status = loginUserDTO.getUserStatus();
        return UserStatus.승인대기.getStatus().equals(status) 
            || UserStatus.휴직중.getStatus().equals(status) 
            || UserStatus.재직중.getStatus().equals(status);
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