package com.ohgiraffers.warehousemanagement.wms.product.service;

import com.ohgiraffers.warehousemanagement.wms.category.model.entity.Category;
import com.ohgiraffers.warehousemanagement.wms.category.model.repository.CategoryRepository;
import com.ohgiraffers.warehousemanagement.wms.product.model.DTO.*;
import com.ohgiraffers.warehousemanagement.wms.product.model.entity.Product;
import com.ohgiraffers.warehousemanagement.wms.product.model.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // 상품 목록 조회 (페이지네이션 및 필터링 적용)
    public ProductPageResponseDTO getAllProducts(int page, int pageSize, String searchKeyword, String category, String storageType, String supplier) {
        if (page < 1) {
            throw new IllegalArgumentException("페이지 번호는 1 이상이어야 합니다.");
        }
        if (pageSize < 1) {
            throw new IllegalArgumentException("페이지 크기는 1 이상이어야 합니다.");
        }

        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Product> productPage = productRepository.findAll(pageable);

        // 필터링 조건 변환
        Integer categoryId = category != null && !category.equals("all") ? Integer.parseInt(category) : null;
        Integer supplierId = supplier != null && !supplier.equals("all") ? Integer.parseInt(supplier) : null;
        String storageTypeValue = storageType != null && !storageType.equals("all") ? storageType : null;
        String searchKeywordValue = searchKeyword != null && !searchKeyword.trim().isEmpty() ? searchKeyword.trim() : null;

        // 메모리 내 필터링
        List<ProductResponseDTO> filteredProducts = productPage.getContent().stream()
                .filter(product -> searchKeywordValue == null || product.getProductName().toLowerCase().contains(searchKeywordValue.toLowerCase()))
                .filter(product -> categoryId == null || product.getCategory().getCategoryId().equals(categoryId))
                .filter(product -> storageTypeValue == null || product.getStorageMethod().equals(storageTypeValue))
                .filter(product -> supplierId == null || product.getSupplierId().equals(supplierId))
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        // 필터링된 결과를 새로운 Page 객체로 변환
        int totalItems = filteredProducts.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);
        List<ProductResponseDTO> pagedProducts = startIndex < totalItems ? filteredProducts.subList(startIndex, endIndex) : List.of();

        int startItem = (page - 1) * pageSize + 1;
        int endItem = Math.min(page * pageSize, totalItems);

        return new ProductPageResponseDTO(pagedProducts, page, totalPages, totalItems, startItem, endItem);
    }

    // 단일 상품 조회
    public ProductResponseDTO getProductById(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("유효하지 않은 상품 ID입니다: " + id);
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품을 찾을 수 없습니다: " + id));
        return convertToResponseDTO(product);
    }

    // 상품 등록
    public ProductResponseDTO createProduct(ProductCreateDTO createDTO) {
        validateCreateDTO(createDTO);
        Product product = new Product();

        Category category = categoryRepository.findById(createDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다: " + createDTO.getCategoryId()));
        product.setCategory(category);

        product.setSupplierId(createDTO.getSupplierId());
        product.setUser_id(createDTO.getUserId());
        product.setProductName(createDTO.getProductName());
        product.setExpirationDate(createDTO.getExpirationDate());
        product.setStorageMethod(createDTO.getStorageMethod());
        product.setPricePerBox(createDTO.getPricePerBox());
        product.setQuantityPerBox(createDTO.getQuantityPerBox());
        product.setProductCreatedAt(LocalDateTime.now());
        product.setDeleted(false);

        Product savedProduct = productRepository.save(product);
        return convertToResponseDTO(savedProduct);
    }

    // 상품 수정
    public ProductResponseDTO updateProduct(Integer id, ProductCreateDTO updateDTO) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("유효하지 않은 상품 ID입니다: " + id);
        }
        validateCreateDTO(updateDTO);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품을 찾을 수 없습니다: " + id));

        Category category = categoryRepository.findById(updateDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다: " + updateDTO.getCategoryId()));
        product.setCategory(category);

        product.setSupplierId(updateDTO.getSupplierId());
        product.setUser_id(updateDTO.getUserId());
        product.setProductName(updateDTO.getProductName());
        product.setExpirationDate(updateDTO.getExpirationDate());
        product.setStorageMethod(updateDTO.getStorageMethod());
        product.setPricePerBox(updateDTO.getPricePerBox());
        product.setQuantityPerBox(updateDTO.getQuantityPerBox());
        product.setProductUpdatedAt(LocalDateTime.now());

        Product updatedProduct = productRepository.save(product);
        return convertToResponseDTO(updatedProduct);
    }

    // 상품 삭제
    public void deleteProduct(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("유효하지 않은 상품 ID입니다: " + id);
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품을 찾을 수 없습니다: " + id));
        product.setDeleted(true);
        product.setProductDeletedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    // DTO 변환
    private ProductResponseDTO convertToResponseDTO(Product product) {
        return new ProductResponseDTO(
                product.getProductId(),
                product.getCategory().getCategoryId(),
                product.getSupplierId(),
                product.getUser_id(),
                product.getProductName(),
                product.getExpirationDate(),
                product.getStorageMethod(),
                product.getPricePerBox(),
                product.getQuantityPerBox(),
                product.getProductCreatedAt(),
                product.getProductUpdatedAt(),
                product.getProductDeletedAt(),
                product.getDeleted()
        );
    }

    // 입력 검증 메서드
    private void validateCreateDTO(ProductCreateDTO dto) {
        if (dto.getProductName() == null || dto.getProductName().trim().isEmpty()) {
            throw new IllegalArgumentException("상품 이름은 필수 입력 항목입니다.");
        }
        if (dto.getCategoryId() == null) {
            throw new IllegalArgumentException("카테고리 ID는 필수 입력 항목입니다.");
        }
        if (dto.getPricePerBox() < 0 || dto.getQuantityPerBox() < 0) {
            throw new IllegalArgumentException("가격과 수량은 음수일 수 없습니다.");
        }
    }

    // 카테고리 목록 조회
    public List<CategoryOptionDTO> getCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryOptionDTO(category.getCategoryId(), category.getCategoryName()))
                .collect(Collectors.toList());
    }

    // 보관방법 목록 조회 (하드코딩 예시)
    public List<StorageTypeOptionDTO> getStorageTypes() {
        List<StorageTypeOptionDTO> storageTypes = new ArrayList<>();
        storageTypes.add(new StorageTypeOptionDTO("상온", "상온"));
        storageTypes.add(new StorageTypeOptionDTO("냉장", "냉장"));
        return storageTypes;
    }

    // 공급업체 목록 조회 (하드코딩 예시)
    public List<SupplierOptionDTO> getSuppliers() {
        List<SupplierOptionDTO> suppliers = new ArrayList<>();
        suppliers.add(new SupplierOptionDTO(1, "공급업체 A"));
        suppliers.add(new SupplierOptionDTO(2, "공급업체 B"));
        return suppliers;
    }
}