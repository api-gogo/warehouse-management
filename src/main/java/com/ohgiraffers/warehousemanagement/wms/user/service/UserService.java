package com.ohgiraffers.warehousemanagement.wms.user.service;

import com.ohgiraffers.warehousemanagement.wms.user.model.common.UserPart;
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

    @Transactional
    public Integer registerUser(SignupUserDTO signupUserDTO) {

        if (userRepository.existsByUserCode(signupUserDTO.getUserCode())) {
            return null;
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

    public LoginUserDTO findbyUserCode(String userCode) {
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
    public boolean updateUser(Integer userId,UserDTO userDTO) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return false;
        }

        if (userDTO.getUserPass() != null && !userDTO.getUserPass().trim().isEmpty()) {
            user.setUserPass(passwordEncoder.encode(userDTO.getUserPass()));
        }

        user.setUserEmail(userDTO.getUserEmail());
        user.setUserPhone(userDTO.getUserPhone());
        user.setUserUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return true;
    }
}
