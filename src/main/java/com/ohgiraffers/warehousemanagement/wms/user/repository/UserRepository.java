package com.ohgiraffers.warehousemanagement.wms.user.repository;

import com.ohgiraffers.warehousemanagement.wms.user.model.common.UserStatus;
import com.ohgiraffers.warehousemanagement.wms.user.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUserCode(String userCode);
    boolean existsByUserCode(String userCode);

    Optional<User> findByUserEmail(String useremail);
    boolean existsByUserEmail(String useremail);

    Optional<User> findByUserPhone(String userphone);
    boolean existsByUserPhone(String userphone);

    long countByUserStatus(UserStatus userStatus);

    @Query("SELECT u FROM User u " +
            "WHERE (:status IS NULL OR u.userStatus = :status) " +
            "AND (:search IS NULL OR " +
            "     lower(u.userName) LIKE lower(concat('%', :search, '%')) OR " +
            "     lower(u.userCode) LIKE lower(concat('%', :search, '%')) OR " +
            "     lower(u.userPhone) LIKE lower(concat('%', :search, '%')))")
    Page<User> findByStatusAndSearch(@Param("status") UserStatus status,
                                          @Param("search") String search,
                                          Pageable pageable);
}
