package com.ohgiraffers.warehousemanagement.wms.product.service;

import com.ohgiraffers.warehousemanagement.wms.product.model.DTO.ProductCreateDTO;
import com.ohgiraffers.warehousemanagement.wms.product.model.DTO.ProductPageResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.product.model.DTO.ProductResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.product.model.entity.Product;
import com.ohgiraffers.warehousemanagement.wms.supplier.model.dto.SupplierDTO;

import java.util.List;

/**
 * 상품 관리 관련 비즈니스 로직을 정의하는 서비스 인터페이스
 */
public interface ProductService {

    /**
     * 주어진 ID로 상품 정보를 조회하여 ProductResponseDTO로 반환
     * @param id 상품 ID
     * @return 조회된 상품 정보를 담은 ProductResponseDTO
     */
    ProductResponseDTO getProductById(Integer id);

    /**
     * 주어진 ID로 상품 엔티티를 조회
     * @param productId 상품 ID
     * @return 조회된 Product 엔티티
     */
    Product findProductById(Integer productId);

    /**
     * 모든 활성 거래처 목록을 조회하여 SupplierDTO 리스트로 반환
     * @return 삭제되지 않은 거래처 정보를 담은 SupplierDTO 리스트
     */
    List<SupplierDTO> getSuppliers();

    /**
     * 모든 상품 목록을 페이징 처리하여 조회
     * @param page 페이지 번호
     * @param pageSize 페이지 크기
     * @param searchKeyword 검색 키워드 (상품명)
     * @return 페이징된 상품 목록을 담은 ProductPageResponseDTO
     */
    ProductPageResponseDTO getAllProducts(int page, int pageSize, String searchKeyword);

    /**
     * 사원이 상품 등록 요청 (대기 상태)
     * @param createDTO 상품 등록 정보
     * @return 등록된 상품 정보를 담은 ProductResponseDTO
     */
    ProductResponseDTO createPending(ProductCreateDTO createDTO);

    /**
     * 사원이 상품 수정 요청 (대기 상태)
     * @param id 상품 ID
     * @param updateDTO 상품 수정 정보
     * @return 수정된 상품 정보를 담은 ProductResponseDTO
     */
    ProductResponseDTO updatePending(Integer id, ProductCreateDTO updateDTO);

    /**
     * 사원이 상품 삭제 요청 (대기 상태)
     * @param id 상품 ID
     */
    void deletePending(Integer id);

    /**
     * 매니저가 상품 즉시 등록
     * @param createDTO 상품 등록 정보
     * @return 등록된 상품 정보를 담은 ProductResponseDTO
     */
    ProductResponseDTO create(ProductCreateDTO createDTO);

    /**
     * 매니저가 상품 즉시 수정
     * @param id 상품 ID
     * @param updateDTO 상품 수정 정보
     * @return 수정된 상품 정보를 담은 ProductResponseDTO
     */
    ProductResponseDTO update(Integer id, ProductCreateDTO updateDTO);

    /**
     * 매니저가 상품 즉시 삭제
     * @param id 상품 ID
     */
    void delete(Integer id);

    /**
     * 매니저가 대기 상태 상품 승인
     * @param id 상품 ID
     * @return 승인된 상품 정보를 담은 ProductResponseDTO
     */
    ProductResponseDTO approveProduct(Integer id);

    /**
     * 매니저가 대기 상태 상품 거절 (삭제 처리)
     * @param id 상품 ID
     */
    void rejectProduct(Integer id);

    /**
     * 관리자가 삭제된 상품 목록 조회
     * @return 삭제된 상품 목록
     */
    List<ProductResponseDTO> findDeleted();

    /**
     * 관리자가 삭제된 상품 복구
     * @param id 상품 ID
     * @return 복구된 상품 정보를 담은 ProductResponseDTO
     */
    ProductResponseDTO restoreProduct(Integer id);

    /**
     * 대기 상태 상품 목록 조회
     * @return 대기 상태 상품 목록
     */
    List<ProductResponseDTO> findPending();
}