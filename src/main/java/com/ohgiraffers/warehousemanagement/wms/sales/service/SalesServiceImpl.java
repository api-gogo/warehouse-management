package com.ohgiraffers.warehousemanagement.wms.sales.service;

import com.ohgiraffers.warehousemanagement.wms.sales.model.dto.SalesDTO;
import com.ohgiraffers.warehousemanagement.wms.sales.model.entity.Sales;
import com.ohgiraffers.warehousemanagement.wms.sales.model.entity.SalesStatus;
import com.ohgiraffers.warehousemanagement.wms.sales.repository.SalesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SalesServiceImpl implements SalesService {
    private final SalesRepository salesRepository;

    @Autowired
    public SalesServiceImpl(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    public List<SalesDTO> getAllSales() {
        // 비즈니스로직 아직 추가안함!!
        List<Sales> findAll = salesRepository.findAll();
        List<SalesDTO> salesLists = new ArrayList<>();
        for (Sales salesEntity : findAll) {
            SalesDTO salesDTO = new SalesDTO(
                    salesEntity.getSalesId(),
                    salesEntity.getStoreId(),
                    salesEntity.getUserId(),
                    salesEntity.getSalesDate(),
                    salesEntity.getShippingDueDate(),
                    salesEntity.getSalesStatus(),
                    salesEntity.getSalesCreatedAt(),
                    salesEntity.getSalesUpdatedAt()
            );
            salesLists.add(salesDTO);
        }
        return salesLists;
    }

    @Transactional
    public SalesDTO createSales(SalesDTO salesDTO) {
        Sales salesEntity = new Sales.Builder()
                .storeId(salesDTO.getStoreId())
                .userId(salesDTO.getUserId())
                .salesDate(salesDTO.getSalesDate())
                .shippingDueDate(salesDTO.getSalesDate().plusDays(3))
                .salesStatus(SalesStatus.PENDING)
                .salesCreatedAt(LocalDateTime.now())
                .build();
        salesRepository.save(salesEntity);

        // 저장 후 상세페이지로 보여주기 때문에 dto로 다시 바꿔줬음
        SalesDTO savedDTO = new SalesDTO(
                salesEntity.getSalesId(),
                salesEntity.getStoreId(),
                salesEntity.getUserId(),
                salesEntity.getSalesDate(),
                salesEntity.getShippingDueDate(),
                salesEntity.getSalesStatus(),
                salesEntity.getSalesCreatedAt(),
                salesEntity.getSalesUpdatedAt() == null ? null : salesEntity.getSalesUpdatedAt()
        );

        return savedDTO;
    }

    @Override
    public Integer getSalesById(Integer salesId) {
        return salesRepository.findById(salesId).get().getSalesId();
    }
}
