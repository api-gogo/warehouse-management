package com.ohgiraffers.warehousemanagement.wms.user.service;

import com.ohgiraffers.warehousemanagement.wms.user.model.common.UserPart;
import com.ohgiraffers.warehousemanagement.wms.user.model.common.UserStatus;
import com.ohgiraffers.warehousemanagement.wms.user.model.dto.LogUserDTO;
import com.ohgiraffers.warehousemanagement.wms.user.model.dto.LoginUserDTO;
import com.ohgiraffers.warehousemanagement.wms.user.model.dto.SignupUserDTO;
import com.ohgiraffers.warehousemanagement.wms.user.model.dto.UserDTO;
import com.ohgiraffers.warehousemanagement.wms.user.model.entity.User;
import com.ohgiraffers.warehousemanagement.wms.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginUserDTO getUserByUserCode(String userCode) {
        Optional<User> user = userRepository.findByUserCode(userCode);

        return user.map(u -> new LoginUserDTO(
                u.getUserId(),
                u.getUserCode(),
                u.getUserPass(),
                u.getUserName(),
                u.getUserPart().getPart(),
                u.getUserRole().getRole(),
                u.getUserStatus().getStatus()
        )).orElse(null);
    }

    public UserDTO getUserByUserId(Integer userId) {
        Optional<User> user = userRepository.findById(userId);

        return user.map(u -> new UserDTO(
                u.getUserId(),
                u.getUserCode(),
                u.getUserPass(),
                u.getUserName(),
                u.getUserEmail(),
                u.getUserPhone(),
                u.getUserPart().getPart(),
                u.getUserRole().getRole(),
                u.getUserStatus().getStatus(),
                u.getUserCreatedAt(),
                u.getUserUpdatedAt(),
                u.getUserDeletedAt()
        )).orElse(null);
    }

    @Transactional
    public Integer registerUser(SignupUserDTO signupUserDTO) {

        if (userRepository.existsByUserCode(signupUserDTO.getUserCode())) {
            return -1;
        }
        else if (userRepository.existsByUserEmail(signupUserDTO.getUserEmail())) {
            return -2;
        }
        else if (userRepository.existsByUserPhone(signupUserDTO.getUserPhone())) {
            return -3;
        }

        try {
            User user = new User(
                    signupUserDTO.getUserCode(),
                    passwordEncoder.encode(signupUserDTO.getUserPass()),
                    signupUserDTO.getUserName(),
                    signupUserDTO.getUserEmail(),
                    signupUserDTO.getUserPhone(),
                    UserPart.valueOf(signupUserDTO.getUserPart())
            );

            User savedUser = userRepository.save(user);
            return savedUser.getUserId();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Transactional
    public boolean updateProfile(Integer userId, UserDTO userDTO) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return false;
        }

        // 비밀번호가 제공된 경우 업데이트
        if (userDTO.getUserPass() != null && !userDTO.getUserPass().trim().isEmpty()) {
            user.setUserPass(passwordEncoder.encode(userDTO.getUserPass()));
        }

        // 일반 필드 업데이트
        user.setUserEmail(userDTO.getUserEmail());
        user.setUserPhone(userDTO.getUserPhone());
        user.setUserUpdatedAt(LocalDateTime.now());
        
        // 승인거부 상태인 경우 승인대기로 변경
        if (user.getUserStatus() == UserStatus.승인거부) {
            user.setUserStatus(UserStatus.승인대기);
        }
        
        userRepository.save(user);
        return true;
    }

    public LogUserDTO getLogUserByUserId(Integer userId) {
        Optional<User> user = userRepository.findById(userId);

        return user.map(u -> new LogUserDTO(
                u.getUserId(),
                u.getUserName(),
                u.getUserPart().getPart(),
                u.getUserRole().getRole(),
                u.getUserStatus().getStatus()
        )).orElse(null);
    }
}
