package com.ohgiraffers.warehousemanagement.wms.product.service;

import com.ohgiraffers.warehousemanagement.wms.auth.model.AuthDetails;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final SupplierService supplierService;
    private final SupplierRepository supplierRepository;
    private final UserService userService;

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

    @Override
    public List<SupplierDTO> getSuppliers() {
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

    @Override
    public ProductPageResponseDTO getAllProducts(int page, int pageSize, String searchKeyword) {
        if (page < 1) {
            throw new IllegalArgumentException("페이지 번호는 1 이상이어야 합니다.");
        }
        if (pageSize < 1) {
            throw new IllegalArgumentException("페이지 크기는 1 이상이어야 합니다.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isManager = authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("상품_매니저"));
        boolean isAdmin = authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("상품_관리자") || a.getAuthority().equals("통합_관리자"));

        List<Product> allProducts;
        if (isAdmin) {
            allProducts = productRepository.findAllWithAllStatuses(); // 관리자는 모든 상태 및 삭제된 상품 조회
        } else if (isManager) {
            allProducts = productRepository.findAllWithAllStatuses(); // 매니저는 모든 상태 조회
        } else {
            // 사원은 APPROVED와 PENDING_* 상태, is_deleted = false만 조회
            allProducts = productRepository.findByStatusIn(Arrays.asList(
                    Product.ProductStatus.APPROVED,
                    Product.ProductStatus.PENDING_CREATE,
                    Product.ProductStatus.PENDING_UPDATE,
                    Product.ProductStatus.PENDING_DELETE
            ));
        }

        String searchKeywordValue = searchKeyword != null && !searchKeyword.trim().isEmpty() ? searchKeyword.trim().toLowerCase() : null;

        List<ProductResponseDTO> filteredProducts = allProducts.stream()
                .filter(product -> searchKeywordValue == null || product.getProductName().toLowerCase().contains(searchKeywordValue))
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

    @Override
    public ProductResponseDTO getProductById(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("유효하지 않은 상품 ID입니다: " + id);
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품을 찾을 수 없습니다: " + id));
        return convertToResponseDTO(product);
    }

    @Override
    public Product findProductById(Integer productId) {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 상품 ID입니다: " + productId);
        }
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품을 찾을 수 없습니다: " + productId));
    }

    @Override
    @Transactional
    public ProductResponseDTO createPending(ProductCreateDTO createDTO) {
        if (createDTO == null) {
            throw new IllegalArgumentException("상품 정보가 제공되지 않았습니다.");
        }

        validateCreateDTO(createDTO);
        Product product = new Product();

        Category category = categoryRepository.findById(createDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리 ID " + createDTO.getCategoryId() + "에 해당하는 카테고리가 존재하지 않습니다."));

        Supplier supplier = supplierRepository.findById(createDTO.getSupplierId())
                .orElseThrow(() -> new IllegalArgumentException("공급업체 ID " + createDTO.getSupplierId() + "에 해당하는 공급업체가 존재하지 않습니다."));

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
        product.setStatus(Product.ProductStatus.PENDING_CREATE);

        Product savedProduct = productRepository.save(product);
        return convertToResponseDTO(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponseDTO updatePending(Integer id, ProductCreateDTO updateDTO) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("유효하지 않은 상품 ID입니다: " + id);
        }
        validateCreateDTO(updateDTO);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품을 찾을 수 없습니다: " + id));

        Category category = categoryRepository.findById(updateDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리 ID " + updateDTO.getCategoryId() + "에 해당하는 카테고리가 존재하지 않습니다."));

        Supplier supplier = supplierRepository.findById(updateDTO.getSupplierId())
                .orElseThrow(() -> new IllegalArgumentException("공급업체 ID " + updateDTO.getSupplierId() + "에 해당하는 공급업체가 존재하지 않습니다."));

        product.setCategory(category);
        product.setSupplier(supplier);
        product.setUserId(updateDTO.getUserId());
        product.setProductName(updateDTO.getProductName());
        product.setExpirationDate(updateDTO.getExpirationDate());
        product.setStorageMethod(updateDTO.getStorageMethod());
        product.setPricePerBox(updateDTO.getPricePerBox());
        product.setQuantityPerBox(updateDTO.getQuantityPerBox());
        product.setProductUpdatedAt(LocalDateTime.now());
        product.setStatus(Product.ProductStatus.PENDING_UPDATE);

        Product updatedProduct = productRepository.save(product);
        return convertToResponseDTO(updatedProduct);
    }

    @Override
    @Transactional
    public void deletePending(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("유효하지 않은 상품 ID입니다: " + id);
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품을 찾을 수 없습니다: " + id));
        product.setStatus(Product.ProductStatus.PENDING_DELETE);
        product.setProductUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    @Override
    @Transactional
    public ProductResponseDTO create(ProductCreateDTO createDTO) {
        if (createDTO == null) {
            throw new IllegalArgumentException("상품 정보가 제공되지 않았습니다.");
        }

        validateCreateDTO(createDTO);
        Product product = new Product();

        Category category = categoryRepository.findById(createDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리 ID " + createDTO.getCategoryId() + "에 해당하는 카테고리가 존재하지 않습니다."));

        Supplier supplier = supplierRepository.findById(createDTO.getSupplierId())
                .orElseThrow(() -> new IllegalArgumentException("공급업체 ID " + createDTO.getSupplierId() + "에 해당하는 공급업체가 존재하지 않습니다."));

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
        product.setStatus(Product.ProductStatus.APPROVED);

        Product savedProduct = productRepository.save(product);
        return convertToResponseDTO(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponseDTO update(Integer id, ProductCreateDTO updateDTO) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("유효하지 않은 상품 ID입니다: " + id);
        }
        validateCreateDTO(updateDTO);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품을 찾을 수 없습니다: " + id));

        Category category = categoryRepository.findById(updateDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리 ID " + updateDTO.getCategoryId() + "에 해당하는 카테고리가 존재하지 않습니다."));

        Supplier supplier = supplierRepository.findById(updateDTO.getSupplierId())
                .orElseThrow(() -> new IllegalArgumentException("공급업체 ID " + updateDTO.getSupplierId() + "에 해당하는 공급업체가 존재하지 않습니다."));

        product.setCategory(category);
        product.setSupplier(supplier);
        product.setUserId(updateDTO.getUserId());
        product.setProductName(updateDTO.getProductName());
        product.setExpirationDate(updateDTO.getExpirationDate());
        product.setStorageMethod(updateDTO.getStorageMethod());
        product.setPricePerBox(updateDTO.getPricePerBox());
        product.setQuantityPerBox(updateDTO.getQuantityPerBox());
        product.setProductUpdatedAt(LocalDateTime.now());
        product.setStatus(Product.ProductStatus.APPROVED);

        Product updatedProduct = productRepository.save(product);
        return convertToResponseDTO(updatedProduct);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("유효하지 않은 상품 ID입니다: " + id);
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품을 찾을 수 없습니다: " + id));
        product.setIsDeleted(true);
        product.setStatus(Product.ProductStatus.DELETED);
        product.setProductDeletedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    @Override
    @Transactional
    public ProductResponseDTO approveProduct(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("유효하지 않은 상품 ID입니다: " + id);
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품을 찾을 수 없습니다: " + id));

        if (product.getStatus() == Product.ProductStatus.PENDING_CREATE ||
                product.getStatus() == Product.ProductStatus.PENDING_UPDATE) {
            product.setStatus(Product.ProductStatus.APPROVED);
        } else if (product.getStatus() == Product.ProductStatus.PENDING_DELETE) {
            product.setIsDeleted(true);
            product.setStatus(Product.ProductStatus.DELETED);
            product.setProductDeletedAt(LocalDateTime.now());
        } else {
            throw new IllegalStateException("승인할 수 없는 상태입니다: " + product.getStatus());
        }

        product.setProductUpdatedAt(LocalDateTime.now());
        Product updatedProduct = productRepository.save(product);
        return convertToResponseDTO(updatedProduct);
    }

    @Override
    @Transactional
    public void rejectProduct(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("유효하지 않은 상품 ID입니다: " + id);
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품을 찾을 수 없습니다: " + id));

        product.setIsDeleted(true);
        product.setStatus(Product.ProductStatus.DELETED);
        product.setProductDeletedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    @Override
    public List<ProductResponseDTO> findDeleted() {
        return productRepository.findByIsDeletedTrue().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductResponseDTO restoreProduct(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("유효하지 않은 상품 ID입니다: " + id);
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품을 찾을 수 없습니다: " + id));

        if (!product.getIsDeleted()) {
            throw new IllegalStateException("이미 복구된 상품입니다.");
        }

        product.setIsDeleted(false);
        product.setStatus(Product.ProductStatus.APPROVED);
        product.setProductDeletedAt(null);
        product.setProductUpdatedAt(LocalDateTime.now());

        Product restoredProduct = productRepository.save(product);
        return convertToResponseDTO(restoredProduct);
    }

    @Override
    public List<ProductResponseDTO> findPending() {
        return productRepository.findByStatusIn(Arrays.asList(
                        Product.ProductStatus.PENDING_CREATE,
                        Product.ProductStatus.PENDING_UPDATE,
                        Product.ProductStatus.PENDING_DELETE
                )).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private ProductResponseDTO convertToResponseDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setProductId(product.getProductId());
        dto.setCategory(product.getCategory());
        dto.setSupplierId(product.getSupplier().getSupplierId());

        SupplierDTO supplier = supplierService.findById(product.getSupplier().getSupplierId());
        dto.setSupplierName(supplier != null ? supplier.getSupplierName() : "-");

        LogUserDTO user = userService.getUserInfoForLogging(product.getUserId());
        dto.setUserName(user != null ? user.getUserName() : "-");
        dto.setUserId(product.getUserId());
        dto.setProductName(product.getProductName());
        dto.setExpirationDate(product.getExpirationDate());
        dto.setStorageMethod(product.getStorageMethod());
        dto.setPricePerBox(product.getPricePerBox());
        dto.setQuantityPerBox(product.getQuantityPerBox());
        dto.setProductCreatedAt(product.getProductCreatedAt());
        dto.setProductUpdatedAt(product.getProductUpdatedAt());
        dto.setProductDeletedAt(product.getProductDeletedAt());
        dto.setIsDeleted(product.getIsDeleted());
        dto.setStatus(product.getStatus());
        return dto;
    }

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
        if (dto.getQuantityPerBox() == null || dto.getQuantityPerBox() <= 0) {
            throw new IllegalArgumentException("박스당 개수는 필수 입력 항목이며 0보다 커야 합니다.");
        }
    }

    public List<CategoryOptionDTO> getCategories() {
        return categoryService.getChildCategories();
    }
}