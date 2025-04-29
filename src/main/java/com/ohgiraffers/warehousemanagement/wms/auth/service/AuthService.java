package com.ohgiraffers.warehousemanagement.wms.auth.service;

import com.ohgiraffers.warehousemanagement.wms.auth.model.AuthDetails;
import com.ohgiraffers.warehousemanagement.wms.user.model.common.UserStatus;
import com.ohgiraffers.warehousemanagement.wms.user.model.dto.LoginUserDTO;
import com.ohgiraffers.warehousemanagement.wms.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public AuthService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String userCode) throws UsernameNotFoundException {
        LoginUserDTO loginUserDTO = userService.findByUserCode(userCode);

        if (Objects.isNull(loginUserDTO)) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }
        
        // UserStatus에 따른 추가 검증
        String userStatus = loginUserDTO.getUserStatus();
        
        // 블랙 또는 퇴사 상태인 경우 로그인 차단
        if (UserStatus.블랙.getStatus().equals(userStatus)) {
            throw new LockedException("차단된 계정입니다. 관리자에게 문의하세요.");
        } else if (UserStatus.퇴사.getStatus().equals(userStatus)) {
            throw new LockedException("퇴사 처리된 계정입니다. 관리자에게 문의하세요.");
        } else if (UserStatus.승인거부.getStatus().equals(userStatus)) {
            // 로그인은 허용하지만 경고 메시지를 콘솔에 출력 (실제 환경에서는 로깅 시스템 사용 권장)
            System.out.println("승인 거부된 계정이 로그인했습니다: " + userCode);
        }

        return new AuthDetails(loginUserDTO);
    }
}