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
import java.util.Arrays;
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

    // 모든 상품 목록 조회 (페이지네이션 및 검색 지원)
    public ProductPageResponseDTO getAllProducts(int page, int pageSize, String searchKeyword) {
        if (page < 1) {
            throw new IllegalArgumentException("페이지 번호는 1 이상이어야 합니다.");
        }
        if (pageSize < 1) {
            throw new IllegalArgumentException("페이지 크기는 1 이상이어야 합니다.");
        }

        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Product> productPage = productRepository.findAll(pageable);

        String searchKeywordValue = searchKeyword != null && !searchKeyword.trim().isEmpty() ? searchKeyword.trim() : null;

        List<ProductResponseDTO> filteredProducts = productPage.getContent().stream()
                .filter(product -> searchKeywordValue == null || product.getProductName().toLowerCase().contains(searchKeywordValue.toLowerCase()))
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        int totalItems = filteredProducts.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);
        List<ProductResponseDTO> pagedProducts = startIndex < totalItems ? filteredProducts.subList(startIndex, endIndex) : List.of();

        int startItem = (page - 1) * pageSize + 1;
        int endItem = Math.min(page * pageSize, totalItems);

        return new ProductPageResponseDTO(pagedProducts, page, totalPages, totalItems, startItem, endItem);
    }

    // 특정 상품 조회
    public ProductResponseDTO getProductById(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("유효하지 않은 상품 ID입니다: " + id);
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품을 찾을 수 없습니다: " + id));
        return convertToResponseDTO(product);
    }

    // 새로운 상품 생성
    public ProductResponseDTO createProduct(ProductCreateDTO createDTO) {
        if (createDTO == null) {
            throw new IllegalArgumentException("상품 정보가 제공되지 않았습니다.");
        }

        validateCreateDTO(createDTO);
        Product product = new Product();

        // 카테고리가 데이터베이스에 존재하는지 확인
        Category category = categoryRepository.findById(createDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리 ID " + createDTO.getCategoryId() + "에 해당하는 카테고리가 존재하지 않습니다."));

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
        System.out.println("상품 저장 완료: " + savedProduct.getProductId() + ", 품명: " + savedProduct.getProductName());
        return convertToResponseDTO(savedProduct);
    }

    // 상품 정보 수정
    public ProductResponseDTO updateProduct(Integer id, ProductCreateDTO updateDTO) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("유효하지 않은 상품 ID입니다: " + id);
        }
        validateCreateDTO(updateDTO);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품을 찾을 수 없습니다: " + id));

        Category category = categoryRepository.findById(updateDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리 ID " + updateDTO.getCategoryId() + "에 해당하는 카테고리가 존재하지 않습니다."));
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

    // 상품 삭제 (논리적 삭제)
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

    // Product 엔티티를 ProductResponseDTO로 변환
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

    // 입력 데이터 유효성 검사
    private void validateCreateDTO(ProductCreateDTO dto) {
        if (dto.getProductName() == null || dto.getProductName().trim().isEmpty()) {
            throw new IllegalArgumentException("상품 이름은 필수 입력 항목입니다.");
        }
        if (dto.getCategoryId() == null) {
            throw new IllegalArgumentException("카테고리 ID는 필수 입력 항목입니다.");
        }
        if (dto.getSupplierId() == null) {
            throw new IllegalArgumentException("공급업체 ID는 필수 입력 항목입니다.");
        }
        if (dto.getUserId() == null) {
            throw new IllegalArgumentException("담당자 ID는 필수 입력 항목입니다.");
        }
        if (dto.getExpirationDate() == null) {
            throw new IllegalArgumentException("유통기한은 필수 입력 항목입니다.");
        }
        if (dto.getStorageMethod() == null || dto.getStorageMethod().trim().isEmpty()) {
            throw new IllegalArgumentException("보관방법은 필수 입력 항목입니다.");
        }
        if (dto.getPricePerBox() == null || dto.getPricePerBox() < 0) {
            throw new IllegalArgumentException("박스당 단가는 필수 입력 항목이며 음수일 수 없습니다.");
        }
        if (dto.getQuantityPerBox() == null || dto.getQuantityPerBox() < 0) {
            throw new IllegalArgumentException("박스당 수량은 필수 입력 항목이며 음수일 수 없습니다.");
        }
    }

    // 카테고리 목록 조회
    /*
    public List<CategoryOptionDTO> getCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryOptionDTO(category.getCategoryId(), category.getCategoryName()))
                .collect(Collectors.toList());
    }
    */

    // 임시 카테고리 목록 제공 (목 데이터)
    public List<CategoryOptionDTO> getMockCategories() {
        return Arrays.asList(
                new CategoryOptionDTO(1, "전자제품"),
                new CategoryOptionDTO(2, "식품"),
                new CategoryOptionDTO(3, "의류")
        );
    }
}