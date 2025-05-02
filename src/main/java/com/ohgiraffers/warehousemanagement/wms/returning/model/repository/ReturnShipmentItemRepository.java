package com.ohgiraffers.warehousemanagement.wms.returning.model.repository;

import com.ohgiraffers.warehousemanagement.wms.returning.model.entity.ReturnShipmentItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ReturnShipmentItemRepository extends JpaRepository<ReturnShipmentItem, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM ReturnShipmentItem i WHERE i.returnShipmentId.returnShipmentId = :returnShipmentId")
    void deleteByReturnShipmentId(@Param("returnShipmentId") Integer returnShipmentId);

}
