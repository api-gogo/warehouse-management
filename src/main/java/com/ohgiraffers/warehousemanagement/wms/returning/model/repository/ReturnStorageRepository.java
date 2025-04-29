package com.ohgiraffers.warehousemanagement.wms.returning.model.repository;

import com.ohgiraffers.warehousemanagement.wms.returning.model.entity.ReturnStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnStorageRepository extends JpaRepository<ReturnStorage,Integer> { //엔티티 이름, 키 타입

}
