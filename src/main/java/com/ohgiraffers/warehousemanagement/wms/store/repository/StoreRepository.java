package com.ohgiraffers.warehousemanagement.wms.store.repository;

import com.ohgiraffers.warehousemanagement.wms.store.model.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Integer> {

    Optional<Store> findByStoreId(Integer storeId);
    boolean existsByStoreId(Integer storeId);

    Optional<Store> findByStoreName(String storeName);
    boolean existsByStoreName(String storeName);
    boolean existsByStoreNameAndStoreIdNot(String storeName, Integer storeId);

    Optional<Store> findByStoreManagerPhone(String storeManagerPhone);
    boolean existsByStoreManagerPhone(String storeManagerPhone);
    boolean existsByStoreManagerPhoneAndStoreIdNot(String storeManagerPhone, Integer storeId);

    Optional<Store> findByStoreManagerEmail(String storeManagerEmail);
    boolean existsByStoreManagerEmail(String storeManagerEmail);
    boolean existsByStoreManagerEmailAndStoreIdNot(String storeManagerEmail, Integer storeId);

    @Query("SELECT s FROM Store s " +
            "WHERE (s.isDeleted = :status) " +
            "AND (:search IS NULL OR " +
            "     lower(s.storeName) LIKE lower(concat('%', :search, '%')) OR " +
            "     lower(s.storeAddress) LIKE lower(concat('%', :search, '%')) OR " +
            "     lower(s.storeManagerName) LIKE lower(concat('%', :search, '%')) OR " +
            "     lower(s.storeManagerEmail) LIKE lower(concat('%', :search, '%')) OR " +
            "     lower(s.storeManagerPhone) LIKE lower(concat('%', :search, '%')))")
    Page<Store> findByStatusAndSearch(@Param("status") boolean status,
                                         @Param("search") String search,
                                         Pageable pageable);

}
