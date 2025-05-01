package com.ohgiraffers.warehousemanagement.wms.product.service;

import com.ohgiraffers.warehousemanagement.wms.category.model.entity.Category;
import com.ohgiraffers.warehousemanagement.wms.category.model.repository.CategoryRepository;
import com.ohgiraffers.warehousemanagement.wms.category.service.CategoryService;
import com.ohgiraffers.warehousemanagement.wms.product.model.DTO.*;
import com.ohgiraffers.warehousemanagement.wms.product.model.entity.Product;
import com.ohgiraffers.warehousemanagement.wms.product.model.repository.ProductRepository;
import com.ohgiraffers.warehousemanagement.wms.supplier.model.dto.SupplierDTO;
import com.ohgiraffers.warehousemanagement.wms.supplier.model.entity.Supplier;
import com.ohgiraffers.warehousemanagement.wms.supplier.repository.SupplierRepository;
import com.ohgiraffers.warehousemanagement.wms.supplier.service.SupplierService;
import com.ohgiraffers.warehousemanagement.wms.user.model.dto.LogUserDTO;
import com.ohgiraffers.warehousemanagement.wms.user.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 상품 관리를 위한 서비스 구현 클래스
 * ProductService 인터페이스의 구현체로, 상품 CRUD 및 관련 비즈니스 로직을 처리합니다.
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final SupplierService supplierService;
    private final SupplierRepository supplierRepository;
    private final UserService userService;

    /**
     * 생성자를 통한 의존성 주입
     * @param productRepository 상품 레포지토리
     * @param categoryRepository 카테고리 레포지토리
     * @param categoryService 카테고리 서비스
     * @param supplierService 공급업체 서비스
     * @param supplierRepository 공급업체 레포지토리
     * @param userService 사용자 서비스
     */
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository,
                            CategoryService categoryService, SupplierService supplierService,
                            SupplierRepository supplierRepository, UserService userService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
        this.supplierService = supplierService;
        this.supplierRepository = supplierRepository;
        this.userService = userService;
    }

    /**
     * 활성화된 모든 공급업체 목록을 조회하는 메서드
     * @return 공급업체 DTO 목록
     */
    @Override
    public List<SupplierDTO> getSuppliers() {
        // 모든 공급업체를 조회하여 삭제되지 않은 공급업체만 필터링하고 DTO로 변환
        return supplierRepository.findAll().stream()
                .filter(supplier -> !supplier.getDeleted())
                .map(supplier -> new SupplierDTO(
                        supplier.getSupplierId(),
                        supplier.getSupplierName(),
                        supplier.getSupplierAddress(),
                        supplier.getSupplierManagerName(),
                        supplier.getSupplierManagerPhone(),
                        supplier.getSupplierManagerEmail(),
                        supplier.getSupplierCreatedAt(),
                        supplier.getSupplierUpdatedAt(),
                        supplier.getSupplierDeletedAt(),
                        supplier.getDeleted()
                ))
                .collect(Collectors.toList());
    }

    /**
     * 상품 목록을 페이징하여 조회하는 메서드
     * @param page 현재 페이지 (1부터 시작)
     * @param pageSize 페이지당 항목 수
     * @param searchKeyword 검색 키워드 (선택 사항)
     * @return 페이징된 상품 정보 (ProductPageResponseDTO)
     * @throws IllegalArgumentException 유효하지 않은 페이지 번호나 페이지 크기가 제공될 경우
     */
    public ProductPageResponseDTO getAllProducts(int page, int pageSize, String searchKeyword) {
        // 입력값 검증
        if (page < 1) {
            throw new IllegalArgumentException("페이지 번호는 1 이상이어야 합니다.");
        }
        if (pageSize < 1) {
            throw new IllegalArgumentException("페이지 크기는 1 이상이어야 합니다.");
        }

        // 모든 상품 조회
        List<Product> allProducts = productRepository.findAll();

        // 검색어 처리
        String searchKeywordValue = searchKeyword != null && !searchKeyword.trim().isEmpty() ? searchKeyword.trim().toLowerCase() : null;

        // 검색 필터링 및 DTO 변환
        List<ProductResponseDTO> filteredProducts = allProducts.stream()
                .filter(product -> searchKeywordValue == null || product.getProductName().toLowerCase().contains(searchKeywordValue))
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        // 페이징 계산
        int totalItems = filteredProducts.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        // 현재 페이지의 상품 목록 추출
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);
        List<ProductResponseDTO> pagedProducts = startIndex < totalItems ? filteredProducts.subList(startIndex, endIndex) : List.of();

        // 페이지 표시를 위한 시작 항목과 끝 항목 인덱스 계산
        int startItem = (page - 1) * pageSize + 1;
        int endItem = Math.min(page * pageSize, totalItems);

        // 페이징 응답 객체 생성 및 반환
        return new ProductPageResponseDTO(pagedProducts, page, totalPages, totalItems, startItem, endItem);
    }

    /**
     * ID로 상품을 조회하는 메서드
     * @param id 상품 ID
     * @return 상품 정보 (ProductResponseDTO)
     * @throws IllegalArgumentException 유효하지 않은 ID가 제공되거나 상품이 존재하지 않을 경우
     */
    @Override
    public ProductResponseDTO getProductById(Integer id) {
        // ID 검증
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("유효하지 않은 상품 ID입니다: " + id);
        }
        // 상품 조회 및 DTO 변환
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품을 찾을 수 없습니다: " + id));
        return convertToResponseDTO(product);
    }

    /**
     * ID로 상품 엔티티를 조회하는 메서드
     * @param productId 상품 ID
     * @return 상품 엔티티 (Product)
     * @throws IllegalArgumentException 유효하지 않은 ID가 제공되거나 상품이 존재하지 않을 경우
     */
    @Override
    public Product findProductById(Integer productId) {
        // ID 검증
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 상품 ID입니다: " + productId);
        }
        // 상품 엔티티 조회 및 반환
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품을 찾을 수 없습니다: " + productId));
    }

    /**
     * 새 상품을 생성하는 메서드
     * @param createDTO 생성할 상품 정보
     * @return 생성된 상품 정보 (ProductResponseDTO)
     * @throws IllegalArgumentException 유효하지 않은 데이터가 제공될 경우
     */
    public ProductResponseDTO createProduct(ProductCreateDTO createDTO) {
        // 입력값 검증
        if (createDTO == null) {
            throw new IllegalArgumentException("상품 정보가 제공되지 않았습니다.");
        }

        validateCreateDTO(createDTO);
        Product product = new Product();

        // 카테고리 조회
        Category category = categoryRepository.findById(createDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리 ID " + createDTO.getCategoryId() + "에 해당하는 카테고리가 존재하지 않습니다."));

        // 공급업체 조회
        Supplier supplier = supplierRepository.findById(createDTO.getSupplierId())
                .orElseThrow(() -> new IllegalArgumentException("공급업체 ID " + createDTO.getSupplierId() + "에 해당하는 공급업체가 존재하지 않습니다."));

        // 상품 정보 설정
        product.setCategory(category);
        product.setSupplier(supplier);
        product.setUserId(createDTO.getUserId());
        product.setProductName(createDTO.getProductName());
        product.setExpirationDate(createDTO.getExpirationDate());
        product.setStorageMethod(createDTO.getStorageMethod());
        product.setPricePerBox(createDTO.getPricePerBox());
        product.setQuantityPerBox(createDTO.getQuantityPerBox());
        product.setProductCreatedAt(LocalDateTime.now());
        product.setIsDeleted(false);

        // 상품 저장 및 결과 반환
        Product savedProduct = productRepository.save(product);
        System.out.println("상품 저장 완료: " + savedProduct.getProductId() + ", 품명: " + savedProduct.getProductName());
        return convertToResponseDTO(savedProduct);
    }

    /**
     * 상품 정보를 수정하는 메서드
     * @param id 수정할 상품의 ID
     * @param updateDTO 수정할 상품 정보
     * @return 수정된 상품 정보 (ProductResponseDTO)
     * @throws IllegalArgumentException 유효하지 않은 ID나 데이터가 제공될 경우
     */
    public ProductResponseDTO updateProduct(Integer id, ProductCreateDTO updateDTO) {
        // ID 및 입력값 검증
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("유효하지 않은 상품 ID입니다: " + id);
        }
        validateCreateDTO(updateDTO);
        
        // 기존 상품 조회
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품을 찾을 수 없습니다: " + id));

        // 카테고리 조회
        Category category = categoryRepository.findById(updateDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리 ID " + updateDTO.getCategoryId() + "에 해당하는 카테고리가 존재하지 않습니다."));

        // 공급업체 조회
        Supplier supplier = supplierRepository.findById(updateDTO.getSupplierId())
                .orElseThrow(() -> new IllegalArgumentException("공급업체 ID " + updateDTO.getSupplierId() + "에 해당하는 공급업체가 존재하지 않습니다."));

        // 상품 정보 업데이트
        product.setCategory(category);
        product.setSupplier(supplier);
        product.setUserId(updateDTO.getUserId());
        product.setProductName(updateDTO.getProductName());
        product.setExpirationDate(updateDTO.getExpirationDate());
        product.setStorageMethod(updateDTO.getStorageMethod());
        product.setPricePerBox(updateDTO.getPricePerBox());
        product.setQuantityPerBox(updateDTO.getQuantityPerBox());
        product.setProductUpdatedAt(LocalDateTime.now());

        // 상품 저장 및 결과 반환
        Product updatedProduct = productRepository.save(product);
        return convertToResponseDTO(updatedProduct);
    }

    /**
     * 상품을 논리적으로 삭제하는 메서드 (실제 삭제 X, 삭제 플래그 설정)
     * @param id 삭제할 상품의 ID
     * @throws IllegalArgumentException 유효하지 않은 ID가 제공되거나 상품이 존재하지 않을 경우
     */
    public void deleteProduct(Integer id) {
        // ID 검증
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("유효하지 않은 상품 ID입니다: " + id);
        }
        
        // 기존 상품 조회
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품을 찾을 수 없습니다: " + id));
        
        // 논리적 삭제 처리 (삭제 플래그 설정 및 삭제 시간 기록)
        product.setIsDeleted(true);
        product.setProductDeletedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    /**
     * 상품 엔티티를 DTO로 변환하는 메서드
     * @param product 변환할 상품 엔티티
     * @return 변환된 상품 DTO (ProductResponseDTO)
     */
    private ProductResponseDTO convertToResponseDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setProductId(product.getProductId());
        dto.setCategory(product.getCategory());
        dto.setSupplierId(product.getSupplier().getSupplierId());

        // 공급업체 정보 설정
        SupplierDTO supplier = supplierService.findById(product.getSupplier().getSupplierId());
        dto.setSupplierName(supplier != null ? supplier.getSupplierName() : "-");

        // 사용자 정보 설정
        LogUserDTO user = userService.getUserInfoForLogging(product.getUserId());
        dto.setUserName(user != null ? user.getUserName() : "-");
        dto.setUserId(product.getUserId());
        
        // 상품 기본 정보 설정
        dto.setProductName(product.getProductName());
        dto.setExpirationDate(product.getExpirationDate());
        dto.setStorageMethod(product.getStorageMethod());
        dto.setPricePerBox(product.getPricePerBox());
        dto.setQuantityPerBox(product.getQuantityPerBox());
        
        // 상품 이력 정보 설정
        dto.setProductCreatedAt(product.getProductCreatedAt());
        dto.setProductUpdatedAt(product.getProductUpdatedAt());
        dto.setProductDeletedAt(product.getProductDeletedAt());
        dto.setIsDeleted(product.getIsDeleted());
        
        return dto;
    }

    /**
     * 상품 생성/수정을 위한 입력값 검증 메서드
     * @param dto 검증할 상품 DTO (ProductCreateDTO)
     * @throws IllegalArgumentException 유효하지 않은 데이터가 있을 경우
     */
    private void validateCreateDTO(ProductCreateDTO dto) {
        // 상품명 검증
        if (dto.getProductName() == null || dto.getProductName().trim().isEmpty()) {
            throw new IllegalArgumentException("상품 이름은 필수 입력 항목입니다.");
        }
        // 카테고리 ID 검증
        if (dto.getCategoryId() == null) {
            throw new IllegalArgumentException("카테고리 ID는 필수 입력 항목입니다.");
        }
        // 공급업체 ID 검증
        if (dto.getSupplierId() == null) {
            throw new IllegalArgumentException("공급업체 ID는 필수 입력 항목입니다.");
        }
        // 담당자 ID 검증
        if (dto.getUserId() == null) {
            throw new IllegalArgumentException("담당자 ID는 필수 입력 항목입니다.");
        }
        // 유통기한 검증
        if (dto.getExpirationDate() == null) {
            throw new IllegalArgumentException("유통기한은 필수 입력 항목입니다.");
        }
        // 보관방법 검증
        if (dto.getStorageMethod() == null || dto.getStorageMethod().trim().isEmpty()) {
            throw new IllegalArgumentException("보관방법은 필수 입력 항목입니다.");
        }
        // 단가 검증
        if (dto.getPricePerBox() == null || dto.getPricePerBox() < 0) {
            throw new IllegalArgumentException("박스당 단가는 필수 입력 항목이며 음수일 수 없습니다.");
        }
        // 박스당 개수 검증
        if (dto.getQuantityPerBox() == null || dto.getQuantityPerBox() <= 0) {
            throw new IllegalArgumentException("박스당 개수는 필수 입력 항목이며 0보다 커야 합니다.");
        }
    }

    /**
     * 카테고리 옵션 목록을 조회하는 메서드
     * @return 카테고리 옵션 DTO 목록
     */
    public List<CategoryOptionDTO> getCategories() {
        return categoryService.getChildCategories();
    }
}