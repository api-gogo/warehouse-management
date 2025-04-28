package com.ohgiraffers.warehousemanagement.wms.category.service;

import com.ohgiraffers.warehousemanagement.wms.category.model.DTO.CategoryDTO;
import com.ohgiraffers.warehousemanagement.wms.category.model.entity.Category;
import com.ohgiraffers.warehousemanagement.wms.category.model.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // 모든 카테고리 목록 조회 (페이지네이션 및 검색 지원, 계층적으로 정렬)
    public Map<String, Object> getAllCategories(int page, int pageSize, String searchKeyword, Integer level) {
        if (page < 1) {
            throw new IllegalArgumentException("페이지 번호는 1 이상이어야 합니다.");
        }
        if (pageSize < 1) {
            throw new IllegalArgumentException("페이지 크기는 1 이상이어야 합니다.");
        }

        // 모든 카테고리를 먼저 조회
        List<Category> allCategories = categoryRepository.findAll();
        logger.info("Total categories from DB: {}", allCategories.size());

        String searchKeywordValue = searchKeyword != null && !searchKeyword.trim().isEmpty() ? searchKeyword.trim().toLowerCase() : null;

        // DTO로 변환
        List<CategoryDTO> allCategoryDTOs = allCategories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        // 대분류와 중분류 분리
        List<CategoryDTO> parentCategories = allCategoryDTOs.stream()
                .filter(cat -> cat.getLevel() == 1)
                .collect(Collectors.toList());

        List<CategoryDTO> childCategories = allCategoryDTOs.stream()
                .filter(cat -> cat.getLevel() == 2)
                .collect(Collectors.toList());

        // 필터링된 카테고리 리스트 생성
        Set<CategoryDTO> filteredCategoriesSet = new LinkedHashSet<>();

        // 검색 조건에 따라 필터링
        for (CategoryDTO parent : parentCategories) {
            boolean parentMatches = searchKeywordValue == null || parent.getCategoryName().toLowerCase().contains(searchKeywordValue);
            boolean levelMatches = level == null || parent.getLevel().equals(level);

            if (parentMatches && levelMatches) {
                // 대분류가 검색 조건에 부합하면 대분류와 모든 중분류를 추가
                filteredCategoriesSet.add(parent);
                childCategories.stream()
                        .filter(child -> child.getParentId() != null && child.getParentId().equals(parent.getCategoryId()))
                        .forEach(filteredCategoriesSet::add);
            } else if (level == null || level == 2) {
                // 대분류가 조건에 부합하지 않더라도, 중분류가 검색 조건에 부합하면 대분류와 중분류를 추가
                List<CategoryDTO> matchingChildren = childCategories.stream()
                        .filter(child -> child.getParentId() != null && child.getParentId().equals(parent.getCategoryId()))
                        .filter(child -> searchKeywordValue == null || child.getCategoryName().toLowerCase().contains(searchKeywordValue))
                        .collect(Collectors.toList());

                if (!matchingChildren.isEmpty()) {
                    filteredCategoriesSet.add(parent);
                    filteredCategoriesSet.addAll(matchingChildren);
                }
            }
        }

        // level이 2일 경우 중분류만 검색 조건에 추가로 필터링
        List<CategoryDTO> filteredCategories = new ArrayList<>(filteredCategoriesSet);
        if (level != null && level == 2) {
            filteredCategories = filteredCategories.stream()
                    .filter(category -> {
                        if (category.getLevel() == 1) {
                            // 대분류는 중분류가 이미 포함되어 있으므로 유지
                            return filteredCategoriesSet.stream()
                                    .anyMatch(child -> child.getLevel() == 2 && child.getParentId() != null && child.getParentId().equals(category.getCategoryId()));
                        }
                        return category.getLevel() == 2;
                    })
                    .collect(Collectors.toList());
        }

        logger.info("Filtered categories size: {}", filteredCategories.size());

        // 계층 구조로 정렬된 리스트 생성 (필터링된 결과 기반)
        List<CategoryDTO> hierarchicalCategories = new ArrayList<>();
        List<CategoryDTO> filteredParents = filteredCategories.stream()
                .filter(cat -> cat.getLevel() == 1)
                .collect(Collectors.toList());

        List<CategoryDTO> filteredChildren = filteredCategories.stream()
                .filter(cat -> cat.getLevel() == 2)
                .collect(Collectors.toList());

        for (CategoryDTO parent : filteredParents) {
            hierarchicalCategories.add(parent);
            filteredChildren.stream()
                    .filter(child -> child.getParentId() != null && child.getParentId().equals(parent.getCategoryId()))
                    .forEach(hierarchicalCategories::add);
        }
        logger.info("Hierarchical categories size: {}", hierarchicalCategories.size());

        // 페이지네이션 처리
        int totalItems = hierarchicalCategories.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        logger.info("Total items: {}, Total pages: {}", totalItems, totalPages);

        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);
        List<CategoryDTO> pagedCategories = startIndex < totalItems ? hierarchicalCategories.subList(startIndex, endIndex) : List.of();
        logger.info("Paged categories size for page {}: {}", page, pagedCategories.size());

        int startPage = Math.max(1, page - 2);
        int endPage = Math.min(totalPages, page + 2);
        if (endPage - startPage < 4) {
            if (startPage == 1) {
                endPage = Math.min(totalPages, startPage + 4);
            } else if (endPage == totalPages) {
                startPage = Math.max(1, endPage - 4);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("categories", pagedCategories);
        result.put("currentPage", page);
        result.put("totalPages", totalPages);
        result.put("totalItems", totalItems);
        result.put("startPage", startPage);
        result.put("endPage", endPage);
        return result;
    }

    // 특정 카테고리 조회
    public CategoryDTO getCategoryById(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("유효하지 않은 카테고리 ID입니다: " + id);
        }
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 카테고리를 찾을 수 없습니다: " + id));
        return convertToDTO(category);
    }

    // 새로운 카테고리 생성
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            throw new IllegalArgumentException("카테고리 정보가 제공되지 않았습니다.");
        }

        validateCategoryDTO(categoryDTO);
        Category category = new Category();
        category.setCategoryName(categoryDTO.getCategoryName());
        category.setParentId(categoryDTO.getParentId() != null ? categoryDTO.getParentId().intValue() : null);
        category.setLevel(categoryDTO.getLevel());
        category.setCategoryCreatedAt(LocalDateTime.now());

        if (category.getLevel() == 2 && category.getParentId() == null) {
            throw new IllegalArgumentException("중분류 카테고리는 상위 카테고리가 필수입니다.");
        }

        if (category.getLevel() == 2) {
            Category parent = categoryRepository.findById(category.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("상위 카테고리 ID " + category.getParentId() + "에 해당하는 카테고리가 존재하지 않습니다."));
            if (parent.getLevel() != 1) {
                throw new IllegalArgumentException("중분류 카테고리의 상위 카테고리는 대분류여야 합니다.");
            }
        }

        Category savedCategory = categoryRepository.save(category);
        return convertToDTO(savedCategory);
    }

    // 카테고리 수정
    public CategoryDTO updateCategory(Integer id, CategoryDTO categoryDTO) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("유효하지 않은 카테고리 ID입니다: " + id);
        }
        validateCategoryDTO(categoryDTO);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 카테고리를 찾을 수 없습니다: " + id));

        category.setCategoryName(categoryDTO.getCategoryName());
        category.setParentId(categoryDTO.getParentId() != null ? categoryDTO.getParentId().intValue() : null);
        category.setLevel(categoryDTO.getLevel());
        category.setCategoryUpdatedAt(LocalDateTime.now());

        if (category.getLevel() == 2 && category.getParentId() == null) {
            throw new IllegalArgumentException("중분류 카테고리는 상위 카테고리가 필수입니다.");
        }

        if (category.getLevel() == 2) {
            Category parent = categoryRepository.findById(category.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("상위 카테고리 ID " + category.getParentId() + "에 해당하는 카테고리가 존재하지 않습니다."));
            if (parent.getLevel() != 1) {
                throw new IllegalArgumentException("중분류 카테고리의 상위 카테고리는 대분류여야 합니다.");
            }
        }

        Category updatedCategory = categoryRepository.save(category);
        return convertToDTO(updatedCategory);
    }

    // 카테고리 삭제
    public void deleteCategory(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("유효하지 않은 카테고리 ID입니다: " + id);
        }
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 카테고리를 찾을 수 없습니다: " + id));

        // 하위 카테고리 삭제
        List<Category> children = categoryRepository.findByParentId(id);
        children.forEach(child -> categoryRepository.delete(child));

        // 카테고리 삭제
        categoryRepository.delete(category);
    }

    // 상위 카테고리 목록 조회 (대분류만)
    public List<CategoryDTO> getParentCategories() {
        return categoryRepository.findByLevel(1).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 상위 카테고리 맵 생성 (ID와 이름 매핑)
    public Map<Integer, String> getParentCategoryMap() {
        List<Category> parentCategories = categoryRepository.findByLevel(1);
        Map<Integer, String> parentCategoryMap = new HashMap<>();
        for (Category parent : parentCategories) {
            parentCategoryMap.put(parent.getCategoryId(), parent.getCategoryName());
        }
        return parentCategoryMap;
    }

    // Category 엔티티를 CategoryDTO로 변환
    private CategoryDTO convertToDTO(Category category) {
        return new CategoryDTO(
                category.getCategoryId().longValue(),
                category.getCategoryName(),
                category.getParentId() != null ? category.getParentId().longValue() : null,
                category.getLevel(),
                category.getCategoryCreatedAt(),
                category.getCategoryUpdatedAt()
        );
    }

    // 입력 데이터 유효성 검사
    private void validateCategoryDTO(CategoryDTO dto) {
        if (dto.getCategoryName() == null || dto.getCategoryName().trim().isEmpty()) {
            throw new IllegalArgumentException("카테고리 이름은 필수 입력 항목입니다.");
        }
        if (dto.getCategoryName().length() > 50) {
            throw new IllegalArgumentException("카테고리 이름은 50자를 초과할 수 없습니다.");
        }
        if (dto.getLevel() == null) {
            throw new IllegalArgumentException("레벨은 필수 입력 항목입니다.");
        }
    }
}