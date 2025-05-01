package com.ohgiraffers.warehousemanagement.wms.purchases.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohgiraffers.warehousemanagement.wms.auth.model.AuthDetails;
import com.ohgiraffers.warehousemanagement.wms.product.model.DTO.ProductResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.product.model.entity.Product;
import com.ohgiraffers.warehousemanagement.wms.product.service.ProductService;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.dto.PurchaseDTO;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.dto.PurchaseItemDTO;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.entity.Purchase;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.entity.PurchaseItem;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.entity.PurchaseStatus;
import com.ohgiraffers.warehousemanagement.wms.purchases.service.PurchaseService;
import com.ohgiraffers.warehousemanagement.wms.purchases.service.PurchasesItemService;
import com.ohgiraffers.warehousemanagement.wms.user.model.dto.LoginUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;

@Controller
@RequestMapping("/purchases")
@Validated
public class PurchaseController {
    private PurchaseService purchaseService;
    private final ObjectMapper objectMapper;
    private final PurchasesItemService purchasesItemService;


    @Autowired
    public PurchaseController(PurchaseService purchaseService, ObjectMapper objectMapper, PurchasesItemService purchasesItemService) {
        this.purchaseService = purchaseService;
        this.objectMapper = objectMapper;
        this.purchasesItemService = purchasesItemService;

    }


    /*
    메인, 전체조회 화면 ,상세 조회 화면 , 발주대기 화면, 발주생성 화면, 발주 수정 화면
     */

    // 메인 페이지
    @GetMapping({"", "/"})
    public String mainPage(Model model) {
        List<PurchaseDTO> purchases = purchaseService.getAllPurchases();
        model.addAttribute("purchases", purchases);
        model.addAttribute("pageTitle", "발주 관리");
        model.addAttribute("cardTitle", "발주 목록");
        model.addAttribute("cardDescription", "모든 발주 내역을 확인하고 관리할 수 있습니다.");

        return "purchases/purchases";
    }


    // 전체조회
    @GetMapping("/list")
    public String getAllPurchases(Model model) {
        List<PurchaseDTO> purchases = purchaseService.getAllPurchases();
        model.addAttribute("purchases", purchases);
        model.addAttribute("pageTitle", "발주 관리");
        model.addAttribute("cardTitle", "발주 목록");
        model.addAttribute("cardDescription", "모든 발주 내역을 확인하고 관리할 수 있습니다.");

        return "purchases/purchases"; // 발주 목록 템플릿 직접 호출
    }

    // status 를 이용하여 조회
    @GetMapping("/search/status")
    public String getAllPurchasesBystatus(Model model, @RequestParam PurchaseStatus status) {
        List<PurchaseDTO> purchase = purchaseService.searchByStatus(status);
        model.addAttribute("purchases", purchase);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("pageTitle", "발주 관리 - " + status + " 조회");
        model.addAttribute("cardTitle", status + " 상태 발주 목록");
        model.addAttribute("cardDescription", status + " 상태의 모든 발주 내역을 확인하고 관리할 수 있습니다.");

        return "purchases/purchases";
    }

    // 담당자 id를 이용하여 조회
    @GetMapping("/search/user")
    public String getPurchasesByUser(Model model, @RequestParam Integer userId) {
        List<PurchaseDTO> purchases = purchaseService.searchByUserId(userId);
        model.addAttribute("purchases", purchases);
        model.addAttribute("selectedUser", userId);
        model.addAttribute("pageTitle", "발주 관리 - 담당자별 조회");
        model.addAttribute("cardTitle", "담당자별 발주 목록");
        model.addAttribute("cardDescription", "선택한 담당자의 발주 내역을 확인하고 관리할 수 있습니다.");

        return "purchases/purchases";
    }


    //기간을 이용하여서 조회
    @GetMapping("/search/date")
    public String getPurchasesByDateRange(
            Model model,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            //DateTimeFormat /search?startDate=2025-04-28 등을 localdate로 변환해줌
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<PurchaseDTO> purchases = purchaseService.searchByPurchaseDateRange(startDate, endDate);
        model.addAttribute("purchases", purchases);
        model.addAttribute("pageTitle", "발주 관리 - 기간별 조회");
        model.addAttribute("cardTitle", "기간별 발주 목록");
        model.addAttribute("cardDescription", startDate + " ~ " + endDate + " 기간의 발주 내역을 확인하고 관리할 수 있습니다.");

        return "purchases/purchases"; // 템플릿 경로 수정
    }


    //단일조회
    @GetMapping("/{id}")
    public String getPurchaseById(@PathVariable Integer id, Model model) {
        PurchaseDTO purchaseDTO = purchaseService.getPurchaseById(id);

        List<PurchaseItemDTO> purchaseItems = purchaseService.getPurchaseItemsByPurchaseId(id);
        //리스트를 통해 purchaseItem을 가져옴(발주의 상세 목록)
        int totalQuantity = purchaseItems.stream().mapToInt(PurchaseItemDTO::getProductQuantity).sum();
        //전체 발주 수량의 총합을 계산
        long totalAmount = purchaseItems.stream()
                .mapToLong(item -> (long) (item.getPricePerBox() != null ? item.getPricePerBox() : 0) * item.getProductQuantity())
                .sum();
        // 총 가격 계산 단가 * 가격
        model.addAttribute("purchase", purchaseDTO);
        model.addAttribute("purchaseItems", purchaseItems);
        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("pageTitle", "발주 상세 정보");


        return "purchases/purchase-detail"; // 새로운 상세 페이지로 이동
    }




    //발주 생성
    @GetMapping("/create")
    public String createPurchase(Model model, @AuthenticationPrincipal AuthDetails authDetails) {
        PurchaseDTO purchaseDTO = new PurchaseDTO();
        // 현재 로그인한 사용자 ID를 미리 설정
        if (authDetails != null) {
            purchaseDTO.setUserId(authDetails.getUserId());
        }
        
        model.addAttribute("purchaseDTO", purchaseDTO);
        model.addAttribute("pageTitle", "발주 생성");
        model.addAttribute("cardTitle", "신규 발주 등록");
        model.addAttribute("cardDescription", "새로운 발주를 등록할 수 있습니다.");

        return "purchases/create"; // 발주 생성 템플릿 경로
    }



    @PostMapping("/create")
    @Transactional  // 트랜잭션 추가 - 발주와 발주항목 저장을 하나의 트랜잭션으로 처리
    public String createPurchase(@Validated @ModelAttribute PurchaseDTO purchaseDTO,
                                 @RequestParam("purchaseItemsData") String purchaseItemsData,
                                 RedirectAttributes rdtat, @AuthenticationPrincipal AuthDetails authDetails) {
        try {
            // 현재 로그인한 사용자 ID 설정
            if (authDetails != null) {
                purchaseDTO.setUserId(authDetails.getUserId());
            } else {
                rdtat.addFlashAttribute("message", "로그인 정보를 찾을 수 없습니다.");
                return "redirect:/purchases/create";
            }
            
            // 디버깅을 위한 로깅 추가
            System.out.println("User ID: " + purchaseDTO.getUserId());
            System.out.println("Purchase Items Data: " + purchaseItemsData);
            
            // JSON 파싱
            List<PurchaseItemDTO> purchaseItems = objectMapper.readValue(purchaseItemsData, new TypeReference<List<PurchaseItemDTO>>() {});
            if (purchaseItems.isEmpty()) {
                rdtat.addFlashAttribute("message", "최소 하나 이상의 상품을 추가해야 합니다.");
                return "redirect:/purchases/create";
            }

            // Purchase 저장
            PurchaseDTO savedPurchase = purchaseService.createPurchase(purchaseDTO);
            System.out.println("Saved Purchase ID: " + savedPurchase.getPurchaseId());

            // PurchaseItem 저장
            for (PurchaseItemDTO item : purchaseItems) {
                item.setPurchaseId(savedPurchase.getPurchaseId());
                purchasesItemService.createPurchaseItem(item);
                System.out.println("Saved Purchase Item: " + item.getProductId() + ", Quantity: " + item.getProductQuantity());
            }

            rdtat.addFlashAttribute("message", "발주가 등록되었습니다.");
        } catch (Exception e) {
            e.printStackTrace(); // 서버 로그에 상세 오류 출력
            rdtat.addFlashAttribute("message", "발주 등록에 실패하였습니다: " + e.getMessage());
            return "redirect:/purchases/create"; // 오류 시 생성 페이지로 다시 이동
        }
        return "redirect:/purchases";
    }




    //발주 수정
    @GetMapping("/purchase-detail/{id}")
    public String updatePurchase(@PathVariable Integer id, Model model) {
        PurchaseDTO purchaseDTO = purchaseService.getPurchaseById(id);
        List<PurchaseItemDTO> purchaseItems = purchaseService.getPurchaseItemsByPurchaseId(id);
        model.addAttribute("purchaseDTO", purchaseDTO);
        model.addAttribute("purchaseItems", purchaseItems);
        model.addAttribute("pageTitle", "발주 수정");
        model.addAttribute("cardTitle", "발주 정보 수정");
        model.addAttribute("cardDescription", "발주 정보를 수정할 수 있습니다.");

        return "purchases/purchase-detail";
    }

    @PostMapping("/purchase-detail/{id}")
    public String updatePurchase(Model model,@PathVariable  Integer id,@Validated @ModelAttribute PurchaseDTO purchaseDTO
            , RedirectAttributes rdtat) {
        PurchaseDTO purchase = purchaseService.updatePurchase(purchaseDTO,id);
        if (purchase != null) {
            rdtat.addFlashAttribute("message", "발주가 수정되었습니다.");
        } else {
            rdtat.addFlashAttribute("message", "발주 수정에 실패했습니다.");
        }
        return "redirect:/purchases";
    }

    // 이 부분 추가: HTML 폼에서 사용하는 URL 패턴과 일치하도록 메소드 추가
    @PostMapping("/update/{id}")
    public String updatePurchaseProcess(@PathVariable Integer id,
                                        @Validated @ModelAttribute PurchaseDTO purchaseDTO,
                                        RedirectAttributes rdtat) {
        // 기존 updatePurchase 메소드와 동일한 로직을 사용
        PurchaseDTO purchase = purchaseService.updatePurchase(purchaseDTO, id);
        if (purchase != null) {
            rdtat.addFlashAttribute("message", "발주가 수정되었습니다.");
        } else {
            rdtat.addFlashAttribute("message", "발주 수정에 실패했습니다.");
        }
        return "redirect:/purchases/" + id;
    }

    // 이 부분 추가: 상품 목록 저장 API 추가
    @PostMapping("/api/items/update")
    @ResponseBody
    public ResponseEntity<?> updatePurchaseItems(@RequestBody Map<String, Object> requestData) {
        try {
            Integer purchaseId = (Integer) requestData.get("purchaseId");
            List<Map<String, Object>> itemsData = (List<Map<String, Object>>) requestData.get("items");

            if (purchaseId == null || itemsData == null) {
                return ResponseEntity.badRequest().body("구매 ID 또는 상품 데이터가 없습니다.");
            }

            // 기존 아이템 모두 삭제
            purchasesItemService.deleteAllByPurchaseId(purchaseId);

            // 새 아이템 추가
            List<PurchaseItemDTO> savedItems = new ArrayList<>();
            for (Map<String, Object> item : itemsData) {
                PurchaseItemDTO itemDTO = new PurchaseItemDTO();
                itemDTO.setPurchaseId(purchaseId);
                itemDTO.setProductId(((Number) item.get("productId")).intValue());
                itemDTO.setProductName((String) item.get("productName"));
                itemDTO.setPricePerBox(((Number) item.get("price")).intValue());
                itemDTO.setProductQuantity(((Number) item.get("quantity")).intValue());

                PurchaseItemDTO savedItem = purchasesItemService.createPurchaseItem(itemDTO);
                savedItems.add(savedItem);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "상품 목록이 성공적으로 저장되었습니다.");
            response.put("items", savedItems);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("상품 목록 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }


    //발주 삭제
    // 관리자,매니저만 삭제로 변경가능
    @PostMapping("/delete/{id}")
    public String deletePurchase(Model model, @PathVariable Integer id, RedirectAttributes rdtat, Authentication authentication) {
        try {
            purchaseService.deletedpurchase(id);
            rdtat.addFlashAttribute("message", "발주가 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            rdtat.addFlashAttribute("message", "삭제할 발주를 찾을 수 없습니다.");
        }
        return "redirect:/purchases";
    }

    //발주 완료(발주완료)
    // 관리자,매니저만 발주완료로 변경가능
    @PostMapping("/complete/{id}")
    public String completePurchase(@PathVariable Integer id,RedirectAttributes rdtat,@ModelAttribute PurchaseDTO purchaseDTO,Authentication authentication) {

        boolean completedPurchase = purchaseService.completePurchase(id);
        if (completedPurchase == true) {
            rdtat.addFlashAttribute("salesDTO", completedPurchase);
            rdtat.addFlashAttribute("message", "발주가 완료되었습니다.");
        } else {
            rdtat.addFlashAttribute("message","발주 완료가 실패 했습니다.");
        }
        return "redirect:/purchases";
    }

    //발주 거절(발주 거절)
    @PostMapping("/reject/{id}")
    public String rejectPurchase(@PathVariable Integer id,RedirectAttributes rdtat,@ModelAttribute PurchaseDTO purchaseDTO) {
        boolean rejectpurchase = purchaseService.rejectPurchase(id);
        if (rejectpurchase == true) {
            rdtat.addFlashAttribute("salesDTO", rejectpurchase);
            rdtat.addFlashAttribute("message", "발주가 거절되었습니다.");
        } else {
            rdtat.addFlashAttribute("message","발주 거절이 실패 했습니다.");
        }
        return "redirect:/purchases";
    }


    //발주 취소(발주 취소) 화면에 메세지 출력후 메인 화면으로 이동
    @PostMapping("/cancel/{id}")
    public String cancelPurchasePost(@PathVariable Integer id, RedirectAttributes rdtat, @ModelAttribute PurchaseDTO purchaseDTO) {
        boolean cancelPurchase = purchaseService.cancelPurchase(id);

        if (cancelPurchase) {
            rdtat.addFlashAttribute("message", "발주가 취소되었습니다.");
        } else {
            rdtat.addFlashAttribute("message", "발주 취소가 실패했습니다.");
        }
        return "redirect:/purchases";
    }

    // GET 방식의 취소 처리 추가 (페이지에서 링크 클릭용)
    @GetMapping("/cancel/{id}")
    public String cancelPurchaseGet(@PathVariable Integer id, RedirectAttributes rdtat) {
        boolean cancelPurchase = purchaseService.cancelPurchase(id);

        if (cancelPurchase) {
            rdtat.addFlashAttribute("message", "발주가 취소되었습니다.");
        } else {
            rdtat.addFlashAttribute("message", "발주 취소가 실패했습니다.");
        }
        return "redirect:/purchases";
    }







//    @GetMapping    페이징
//    public String listPurchases(
//            @RequestParam(required = false) String keyword,
//            @RequestParam(required = false) String status,
//            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
//            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "purchaseCreatedAt") String sortField,
//            @RequestParam(defaultValue = "desc") String sortDirection,
//            Model model) {
//
//        // 정렬 방향 설정
//        Sort.Direction direction = sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
//
//        // 페이징 및 정렬 설정
//        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
//
//        // 서비스 호출하여 페이징 처리된 발주 목록 조회
//        Page<Purchase> purchasePage = purchaseService.searchPurchasesWithPagination(
//                keyword, status, startDate, endDate, pageable);
//
//        // 모델에 데이터 추가
//        model.addAttribute("purchases", purchasePage.getContent());
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", purchasePage.getTotalPages());
//        model.addAttribute("totalItems", purchasePage.getTotalElements());
//        model.addAttribute("pageSize", size);
//        model.addAttribute("sortField", sortField);
//        model.addAttribute("sortDirection", sortDirection);
//        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
//        model.addAttribute("keyword", keyword);
//        model.addAttribute("status", status);
//        model.addAttribute("startDate", startDate);
//        model.addAttribute("endDate", endDate);
//
//        return "purchases/list";
//    }

//    @PostMapping
//    public String getProductDetails(@RequestParam Integer id) {
//        List<Product> product = purchaseService.getProductDetails() ;
//     }





    @GetMapping("/api/products/{productId}")
    @ResponseBody
    public ResponseEntity<?> getProductInfo(@PathVariable Integer productId) {
        try {
            PurchaseItemDTO product = purchaseService.getProductInfo(productId);

            if (product == null) {
                System.out.println("상품 정보를 찾을 수 없습니다. ID: " + productId);
                return ResponseEntity.notFound().build();
            }

            System.out.println("조회된 상품 정보: " + product.toString());

            // ProductResponseDTO를 직접 반환
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            System.err.println("상품 정보 조회 오류: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("상품 정보를 가져오는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}