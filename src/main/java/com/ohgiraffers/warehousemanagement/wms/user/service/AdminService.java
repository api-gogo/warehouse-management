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

    public Page<UserDTO> getUsersByPartRoleAndStatus(String search, String partTab, String roleTab, String statusTab, Pageable pageable) {
        UserStatus userStatus = null;
        UserRole userRole = null;
        UserPart userPart = null;
        
        // 상태값 변환 로직
        if (statusTab != null && !statusTab.equals("all")) {
            try {
                userStatus = UserStatus.valueOf(statusTab);
            } catch (IllegalArgumentException e) {
                // 기본값은 null로 모든 상태 조회
            }
        }
        
        // 역할값 변환 로직
        if (roleTab != null && !roleTab.equals("all")) {
            try {
                userRole = UserRole.valueOf(roleTab);
            } catch (IllegalArgumentException e) {
                // 기본값은 null로 모든 역할 조회
            }
        }
        
        // 부서값 변환 로직
        if (partTab != null && !partTab.equals("all")) {
            try {
                userPart = UserPart.valueOf(partTab);
            } catch (IllegalArgumentException e) {
                // 기본값은 null로 모든 부서 조회
            }
        }
        
        Page<User> userPage = userRepository.findByFilters(userPart, userRole, userStatus, search, pageable);

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

    public Page<UserDTO> getPendingUsers(String search, Pageable pageable) {
        Page<User> userPage = userRepository.findByStatusAndSearch(UserStatus.승인대기, search, pageable);

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
    public boolean updateUser(Long userId, UserDTO userDTO) {
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
    public boolean approveUser(Long userId, UserDTO userDTO) {
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
    public boolean approveUsers(List<Long> userIds, String batchPart, String batchRole) {
        if (userIds == null || userIds.isEmpty()) {
            return false;
        }

        List<User> users = new ArrayList<>();

        for (Long id : userIds) {
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
    public boolean rejectUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return false;
        }

        user.setUserStatus(UserStatus.승인거부);
        user.setUserUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return true;
    }

    @Transactional
    public boolean rejectUsers(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return false;
        }

        List<User> users = new ArrayList<>();

        for (Long id : userIds) {
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
        }

        userRepository.saveAll(users);
        return true;
    }

    @Transactional
    public boolean disableUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return false;
        }

        user.setUserStatus(UserStatus.휴직중);
        user.setUserUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return true;
    }

    @Transactional
    public boolean enableUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return false;
        }

        user.setUserStatus(UserStatus.재직중);
        user.setUserUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return true;
    }

    @Transactional
    public boolean resignUser(Long userId) {
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
    
    /**
     * 사용자를 블랙 상태로 변경
     * 지속적으로 부적절한 행동을 하는 사용자를 완전히 차단하기 위한 기능
     */
    @Transactional
    public boolean blacklistUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return false;
        }

        user.setUserStatus(UserStatus.블랙);
        user.setUserUpdatedAt(LocalDateTime.now());
        user.setUserDeletedAt(LocalDateTime.now());
        userRepository.save(user);
        return true;
    }
    
    /**
     * 사용자의 상태가 승인거부인 경우 승인대기로 변경
     * 관리자가 수동으로 승인거부된 사용자에게 재승인 기회를 주기 위한 기능
     */
    @Transactional
    public boolean resetToApprovalPending(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null || user.getUserStatus() != UserStatus.승인거부) {
            return false;
        }

        user.setUserStatus(UserStatus.승인대기);
        user.setUserUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return true;
    }
}