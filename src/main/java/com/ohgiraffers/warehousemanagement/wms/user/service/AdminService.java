package com.ohgiraffers.warehousemanagement.wms.user.service;

import com.ohgiraffers.warehousemanagement.wms.user.model.common.UserPart;
import com.ohgiraffers.warehousemanagement.wms.user.model.common.UserRole;
import com.ohgiraffers.warehousemanagement.wms.user.model.common.UserStatus;
import com.ohgiraffers.warehousemanagement.wms.user.model.dto.UserDTO;
import com.ohgiraffers.warehousemanagement.wms.user.model.entity.User;
import com.ohgiraffers.warehousemanagement.wms.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;

    @Autowired
    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<UserDTO> findUsers(String search, String status, Pageable pageable) {
        UserStatus userStatus = null;
        
        // 상태값 변환 로직
        if (status != null && !status.equals("all")) {
            switch (status) {
                case "pending":
                    userStatus = UserStatus.승인대기;
                    break;
                case "active":
                    userStatus = UserStatus.재직중;
                    break;  
                case "inactive":
                    userStatus = UserStatus.휴직중;
                    break;
                case "rejected":
                    userStatus = UserStatus.승인거부;
                    break;
                case "resigned":
                    userStatus = UserStatus.퇴사;
                    break;
                default:
                    // 기본값은 null로 모든 상태 조회
                    break;
            }
        }
        
        Page<User> userPage = userRepository.findUsersByStatusAndSearch(userStatus, search, pageable);

        return userPage.map(user -> new UserDTO(
                user.getUserId(),
                user.getUserCode(),
                user.getUserPass(),
                user.getUserName(),
                user.getUserEmail(),
                user.getUserPhone(),
                user.getUserPart().getPart(),
                user.getUserRole().getRole(),
                user.getUserStatus().getStatus(),
                user.getUserCreatedAt(),
                user.getUserUpdatedAt(),
                user.getUserDeletedAt()
        ));
    }

    public Page<UserDTO> findPendingUsers(String search, Pageable pageable) {
        Page<User> userPage = userRepository.findUsersByStatusAndSearch(UserStatus.승인대기, search, pageable);

        return userPage.map(user -> new UserDTO(
                user.getUserId(),
                user.getUserCode(),
                user.getUserPass(),
                user.getUserName(),
                user.getUserEmail(),
                user.getUserPhone(),
                user.getUserPart().getPart(),
                user.getUserRole().getRole(),
                user.getUserStatus().getStatus(),
                user.getUserCreatedAt(),
                user.getUserUpdatedAt(),
                user.getUserDeletedAt()
        ));
    }

    public long countPendingUsers() {
        return userRepository.countByUserStatus(UserStatus.승인대기);
    }

    @Transactional
    public boolean updateUser(Integer userId, UserDTO userDTO) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return false;
        }

        user.setUserEmail(userDTO.getUserEmail());
        user.setUserPhone(userDTO.getUserPhone());
        user.setUserPart(UserPart.valueOf(userDTO.getUserPart()));
        user.setUserRole(UserRole.valueOf(userDTO.getUserRole()));
        user.setUserStatus(UserStatus.valueOf(userDTO.getUserStatus()));
        user.setUserUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return true;
    }

    @Transactional
    public boolean approveUser(Integer userId, UserDTO userDTO) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return false;
        }

        user.setUserPart(UserPart.valueOf(userDTO.getUserPart()));
        user.setUserRole(UserRole.valueOf(userDTO.getUserRole()));
        user.setUserStatus(UserStatus.재직중);
        user.setUserUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return true;
    }

    @Transactional
    public boolean approveUsers(List<Integer> userIds, String batchPart, String batchRole) {
        if (userIds == null || userIds.isEmpty()) {
            return false;
        }

        List<User> users = new ArrayList<>();

        for (Integer id : userIds) {
            User user = userRepository.findById(id).orElse(null);
            if (user == null) {
                return false;
            }

            users.add(user);
        }

        try {
            UserPart userPart = UserPart.valueOf(batchPart);
            UserRole userRole = UserRole.valueOf(batchRole);
            
            for (User user : users) {
                user.setUserStatus(UserStatus.재직중);
                user.setUserPart(userPart);
                user.setUserRole(userRole);
                user.setUserUpdatedAt(LocalDateTime.now());
            }

            userRepository.saveAll(users);
            return true;
        } catch (IllegalArgumentException e) {
            // 부서나 역할 열거형 예외 처리
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public boolean rejectUser(Integer userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return false;
        }

        user.setUserStatus(UserStatus.승인거부);
        user.setUserUpdatedAt(LocalDateTime.now());
        user.setUserDeletedAt(LocalDateTime.now());
        userRepository.save(user);
        return true;
    }

    @Transactional
    public boolean rejectUsers(List<Integer> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return false;
        }

        List<User> users = new ArrayList<>();

        for (Integer id : userIds) {
            User user = userRepository.findById(id).orElse(null);
            if (user == null) {
                return false;
            }

            users.add(user);
        }

        LocalDateTime now = LocalDateTime.now();
        for (User user : users) {
            user.setUserStatus(UserStatus.승인거부);
            user.setUserUpdatedAt(now);
            user.setUserDeletedAt(now);
        }

        userRepository.saveAll(users);
        return true;
    }

    @Transactional
    public boolean disableUser(Integer userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return false;
        }

        user.setUserStatus(UserStatus.휴직중);
        user.setUserUpdatedAt(LocalDateTime.now());
        user.setUserDeletedAt(LocalDateTime.now());
        userRepository.save(user);
        return true;
    }

    @Transactional
    public boolean enableUser(Integer userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return false;
        }

        user.setUserStatus(UserStatus.재직중);
        user.setUserUpdatedAt(LocalDateTime.now());
        user.setUserDeletedAt(LocalDateTime.now());
        userRepository.save(user);
        return true;
    }

    @Transactional
    public boolean resignUser(Integer userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return false;
        }

        user.setUserStatus(UserStatus.퇴사);
        user.setUserUpdatedAt(LocalDateTime.now());
        user.setUserDeletedAt(LocalDateTime.now());
        userRepository.save(user);
        return true;
    }
}
