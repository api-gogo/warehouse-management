package com.ohgiraffers.warehousemanagement.wms.auth.service;

import com.ohgiraffers.warehousemanagement.wms.auth.model.AuthDetails;
import com.ohgiraffers.warehousemanagement.wms.user.model.dto.LoginUserDTO;
import com.ohgiraffers.warehousemanagement.wms.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
        LoginUserDTO loginUserDTO = userService.findbyUserCode(userCode);

        if (Objects.isNull(loginUserDTO)) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        return new AuthDetails(loginUserDTO);
    }
}
