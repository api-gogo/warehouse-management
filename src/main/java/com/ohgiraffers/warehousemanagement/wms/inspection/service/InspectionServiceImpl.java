package com.ohgiraffers.warehousemanagement.wms.inspection.service;

import com.ohgiraffers.warehousemanagement.wms.inspection.model.common.InspectionStatus;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.common.InspectionTransactionType;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.dto.request.InspectionRequestDTO;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.dto.response.InspectionResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.dto.response.SearchResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.entity.Inspection;
import com.ohgiraffers.warehousemanagement.wms.inspection.repository.InspectionRepository;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.entity.Purchase;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.entity.PurchaseItem;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.repository.PurchaseItemRepository;
import com.ohgiraffers.warehousemanagement.wms.returning.ReturnShipmentStatus;
import com.ohgiraffers.warehousemanagement.wms.returning.model.entity.ReturnShipment;
import com.ohgiraffers.warehousemanagement.wms.returning.model.entity.ReturnShipmentItem;
import com.ohgiraffers.warehousemanagement.wms.returning.model.repository.ReturnShipmentItemRepository;
import com.ohgiraffers.warehousemanagement.wms.returning.model.repository.ReturnShipmentRepository;
import com.ohgiraffers.warehousemanagement.wms.sales.model.entity.Sales;
import com.ohgiraffers.warehousemanagement.wms.sales.model.entity.SalesItem;
import com.ohgiraffers.warehousemanagement.wms.sales.model.entity.SalesStatus;
import com.ohgiraffers.warehousemanagement.wms.sales.repository.SalesItemsRepository;
import com.ohgiraffers.warehousemanagement.wms.sales.repository.SalesRepository;
import com.ohgiraffers.warehousemanagement.wms.storage.model.entity.Storage;
import com.ohgiraffers.warehousemanagement.wms.storage.model.repository.StorageRepository;
import com.ohgiraffers.warehousemanagement.wms.user.model.entity.User;
import com.ohgiraffers.warehousemanagement.wms.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InspectionServiceImpl implements InspectionService {
    private final InspectionRepository inspectionRepository;
    private final UserRepository userRepository;

    // 입고 관련 repository
    private final PurchaseItemRepository purchaseItemRepository;
    private final StorageRepository storageRepository;

    // 출고반품 관련 repository
    private final ReturnShipmentItemRepository returnShipmentItemRepository;
    private final ReturnShipmentRepository returnShipmentRepository;

    // 수주 관련 repository
    private final SalesItemsRepository salesItemsRepository;
    private final SalesRepository salesRepository;

    @Autowired
    public InspectionServiceImpl(InspectionRepository inspectionRepository, UserRepository userRepository,
                                 PurchaseItemRepository purchaseItemRepository, StorageRepository storageRepository
    , ReturnShipmentItemRepository returnShipmentItemRepository, ReturnShipmentRepository returnShipmentRepository
    , SalesItemsRepository salesItemsRepository, SalesRepository salesRepository) {
        this.inspectionRepository = inspectionRepository;
        this.userRepository = userRepository;
        this.purchaseItemRepository = purchaseItemRepository;
        this.storageRepository = storageRepository;
        this.returnShipmentItemRepository = returnShipmentItemRepository;
        this.returnShipmentRepository = returnShipmentRepository;
        this.salesItemsRepository = salesItemsRepository;
        this.salesRepository = salesRepository;
    }

    @Override
    @Transactional
    public InspectionResponseDTO createInspection(InspectionRequestDTO requestDTO) {
        if(requestDTO.getTransactionId() != null && !requestDTO.getTransactionType().equals(InspectionTransactionType.INSPECTION)) {
            Optional<Inspection> findInspection = inspectionRepository.findByTransactionTypeAndTransactionId(
                    requestDTO.getTransactionType(), requestDTO.getTransactionId()
            );
            if (findInspection.isPresent()) {
                throw new IllegalArgumentException("이미 검수했습니다.\n" +
                        "검수 유형 : " + requestDTO.getTransactionType().getTransactionType() +
                        "\n검수 ID : " + requestDTO.getTransactionId());
            }
        }

        Inspection inspection = new Inspection(requestDTO);
        Optional<User> userById = userRepository.findById(requestDTO.getUserId());
        if (userById.isPresent()) {
            inspection.setUser(userById.get());
        } else {
            throw new IllegalArgumentException("유저 정보가 없습니다.\n" +
                    "조회한 유저 ID : " + requestDTO.getUserId());
        }

        Inspection saveInspection = inspectionRepository.save(inspection);

        switch (saveInspection.getTransactionType()) {
            case PURCHASE -> {
                return changeStorageStatus(saveInspection);
            }
            case SALES -> {
                return changeShipments(saveInspection);
            }
            case SHIPMENT -> {
                return changeReturnShipments(saveInspection);
            }
        }

        return new InspectionResponseDTO(saveInspection);
    }

    public Page<InspectionResponseDTO> getAllInspection(SearchResponseDTO dto, int page, int size) {
        Page<Inspection> inspectionList = null;
        Pageable pageable = PageRequest.of(page - 1, size);
            if(dto.getSearchType() == null || dto.getSearchType().trim().isEmpty()) {
                inspectionList = inspectionRepository.findAllByOrderByInspectionIdDesc(pageable);
            } else {
                inspectionList = inspectionRepository.findAllBySearchOrderByInspectionIdDesc(dto.getSearchType(), dto.getSearch(), pageable);
            }

        return inspectionList.map(InspectionResponseDTO::new);
    }

    public Page<InspectionResponseDTO> getAllInspectionByTag(SearchResponseDTO dto, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Inspection> inspectionList = null;
        if (InspectionTransactionType.typeContains(dto.getInspectionType())) {
            if(dto.getSearchType() == null || dto.getSearchType().trim().isEmpty()) {
                inspectionList = inspectionRepository.findAllByTransactionTypeOrderByInspectionIdDesc(InspectionTransactionType.valueOf(dto.getInspectionType()), pageable);
            } else {
                inspectionList = inspectionRepository.findAllByTransactionTypeAndSearchOrderByInspectionIdDesc(
                        InspectionTransactionType.valueOf(dto.getInspectionType()),
                        dto.getSearchType(), dto.getSearch(), pageable);
            }
            return inspectionList.map(InspectionResponseDTO::new);
        }
        else
            throw new IllegalArgumentException("존재하지 않는 유형입니다!");
    }

    public InspectionResponseDTO getInspectionById(Long inspectionId) {
        Optional<Inspection> findInspection = inspectionRepository.findById(inspectionId);
        if (findInspection.isPresent()) {
            return new InspectionResponseDTO(findInspection.get());
        } else {
            throw new IllegalArgumentException("존재하지 않는 검수 ID입니다! \n" +
                    "검색 ID : " + inspectionId);
        }
    }

    @Override
    public InspectionResponseDTO getInspectionByTagAndTagId(String type, Long typeId) {
        if (!InspectionTransactionType.typeContains(type))
            throw new IllegalArgumentException("존재하지 않는 유형입니다!");

        Optional<Inspection> findInspection = inspectionRepository.findByTransactionTypeAndTransactionId(
                InspectionTransactionType.valueOf(type), typeId);

        if (findInspection.isPresent()) {
            return new InspectionResponseDTO(findInspection.get());
        } else {
            throw new IllegalArgumentException("존재하지 않는 검수유형 ID 입니다! \n" +
                    "검수 유형 : " + type +
                    "\n검수 ID : " + typeId);
        }
    }

    @Transactional
    public InspectionResponseDTO updateInspection(Long inspectionId, InspectionRequestDTO requestDTO) {
        if(requestDTO.getTransactionId() != null && !requestDTO.getTransactionType().equals(InspectionTransactionType.INSPECTION)) {
            Optional<Inspection> findDTOInspection = inspectionRepository.findByTransactionTypeAndTransactionId(
                    requestDTO.getTransactionType(), requestDTO.getTransactionId()
            );
            if (findDTOInspection.isPresent()) {
                if(!inspectionId.equals(findDTOInspection.get().getInspectionId()))
                    throw new IllegalArgumentException("이미 검수했습니다.\n" +
                            "검수 유형 : " + requestDTO.getTransactionType().getTransactionType() +
                            "\n검수 ID : " + requestDTO.getTransactionId());
            }
        }

        Inspection saveInspection = null;
        Optional<Inspection> findInspection = inspectionRepository.findById(inspectionId);
        if (findInspection.isPresent()) {
            Inspection inspection = findInspection.get();
            Inspection dtoToInspection = new Inspection(requestDTO);
            dtoToInspection.setUser(inspection.getUser());

            dtoToInspection.setInspectionId(inspection.getInspectionId());
            dtoToInspection.setInspectionUpdatedAt(inspection.getInspectionUpdatedAt());

            if(inspection.equals(dtoToInspection))
                throw new IllegalArgumentException("변경사항이 없습니다!");
            else {
                dtoToInspection.setInspectionUpdatedAt(LocalDateTime.now());
                saveInspection = inspectionRepository.save(dtoToInspection);
            }
        } else {
            throw new IllegalArgumentException("존재하지 않는 검수 ID입니다! \n" +
                    "검색 ID : " + inspectionId);
        }

        switch (saveInspection.getTransactionType()) {
            case PURCHASE -> {
                return changeStorageStatus(saveInspection);
            }
            case SALES -> {
                return changeShipments(saveInspection);
            }
            case SHIPMENT -> {
                return changeReturnShipments(saveInspection);
            }
        }

        return new InspectionResponseDTO(saveInspection);
    }

    public void deleteInspection(Long inspectionId) {
        Optional<Inspection> findInspection = inspectionRepository.findById(inspectionId);
        if (findInspection.isPresent()) {
            Inspection inspection = findInspection.get();
            inspectionRepository.delete(inspection);
        } else {
            throw new IllegalArgumentException("존재하지 않는 검수 ID입니다! \n" +
                    "검수 ID : " + inspectionId);
        }
    }

    private InspectionResponseDTO changeStorageStatus(Inspection saveInspection) {
        // 발주의 경우 발주 목록에 개수만큼 채워지면 입고에 이상없음으로 변경해주기
        Optional<PurchaseItem> purchaseItemOptional = purchaseItemRepository.findById(saveInspection.getTransactionId());
        if(purchaseItemOptional.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 발주 목록 ID입니다.\n" +
                    "요청 발주 ID : " + saveInspection.getTransactionId());
        }
        Purchase purchase = purchaseItemOptional.get().getPurchase();
        List<Storage> byPurchaseId = storageRepository.findByPurchase_PurchaseId(purchase.getPurchaseId());
        if (saveInspection.getInspectionStatus().equals(InspectionStatus.DEFECTIVE)) {
            // 검수 도중 이상을 발견했으므로 검수 이상으로 리턴하기
            for (Storage storage : byPurchaseId) {
                storage.setInspectionStatus("검수 이상");
                storageRepository.save(storage);

                return new InspectionResponseDTO(saveInspection);
            }
        }
        int count = 0;
        for (PurchaseItem item : purchase.getItems()) {
            Optional<Inspection> inspectionOptional = inspectionRepository.findByTransactionTypeAndTransactionId(InspectionTransactionType.PURCHASE, item.getProductId());
            if (inspectionOptional.isPresent()) {
                count++;
            } else {
                break;
            }
        }
        if (count == purchase.getItems().size()) {
            if (byPurchaseId.isEmpty()) {
                throw new IllegalArgumentException("입고가 등록되지 않았습니다!");
            }
            for (Storage storage : byPurchaseId) {
                storage.setInspectionStatus("검수 완료");
                storageRepository.save(storage);
            }
        }
        return new InspectionResponseDTO(saveInspection);
    }

    private InspectionResponseDTO changeReturnShipments(Inspection saveInspection) {
        // 출고반품 경우 출고반품 목록에 개수만큼 채워지면 입고에 이상없음으로 변경해주기
        Optional<ReturnShipmentItem> shipmentItemOptional = returnShipmentItemRepository.findById(saveInspection.getTransactionId().intValue()); // 강제형변환 한거라 좋지 않음, 나중에 Id를 Long 타입으로 변경 필요
        if(shipmentItemOptional.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 출고반품 목록 ID입니다.\n" +
                    "요청 출고반품 목록 ID : " + saveInspection.getTransactionId());
        }
        ReturnShipment shipment = shipmentItemOptional.get().getReturnShipmentId();
        if (saveInspection.getInspectionStatus().equals(InspectionStatus.DEFECTIVE)) {
            // 검수 도중에 이상을 발견했으므로 반품 거절로 변경하기
            shipment.setReturnShipmentStatus(ReturnShipmentStatus.RETURN_REJECTED); // 반품 거절
            returnShipmentRepository.save(shipment);

            return new InspectionResponseDTO(saveInspection);
        }

        List<ReturnShipmentItem> returnShipmentItems = shipment.getReturnShipmentItems();
        int count = 0;
        for (ReturnShipmentItem item : returnShipmentItems) {
            Optional<Inspection> inspectionOptional = inspectionRepository.findByTransactionTypeAndTransactionId(InspectionTransactionType.SHIPMENT, item.getReturnShipmentItemId());
            if (inspectionOptional.isPresent()) {
                count++;
            } else {
                break;
            }
        }

        if (count == returnShipmentItems.size()) {
            shipment.setReturnShipmentStatus(ReturnShipmentStatus.RETURN_COMPLETED); // 반품 완료
            returnShipmentRepository.save(shipment);
        }

        return new InspectionResponseDTO(saveInspection);
    }

    private InspectionResponseDTO changeShipments(Inspection saveInspection) {
        // 수주반품의 경우 수주상품 목록에 개수만큼 채워지면 출고에 이상없음으로 변경하기
        Optional<SalesItem> salesItemOptional = salesItemsRepository.findById(saveInspection.getTransactionId().intValue()); // 강제형변환 한거라 좋지 않음, 나중에 Id를 Long 타입으로 변경 필요
        if(salesItemOptional.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 수주상품 ID 입니다.\n" +
                    "요청 수주상품 목록 ID : " + saveInspection.getTransactionId());
        }
        Sales sales = salesItemOptional.get().getSalesId();
        // 검수 도중에 이상을 발견했으므로 출고 지연으로 변경하기
        if (saveInspection.getInspectionStatus().equals(InspectionStatus.DEFECTIVE)) {
            sales.setSalesStatus(SalesStatus.PENDING); // 보류중? 으로 변경
            salesRepository.save(sales);

            return new InspectionResponseDTO(saveInspection);
        }

        List<SalesItem> salesItems = sales.getSalesItems();
        int count = 0;
        for (SalesItem item : salesItems) {
            Optional<Inspection> inspectionOptional = inspectionRepository.findByTransactionTypeAndTransactionId(InspectionTransactionType.SHIPMENT, item.getSalesItemsId());
            if (inspectionOptional.isPresent()) {
                count++;
            } else {
                break;
            }
        }

        if (count == salesItems.size()) {
            sales.setSalesStatus(SalesStatus.APPROVED); // 승인됨? 으로 변경
            salesRepository.save(sales);
        }

        return new InspectionResponseDTO(saveInspection);
    }
}
