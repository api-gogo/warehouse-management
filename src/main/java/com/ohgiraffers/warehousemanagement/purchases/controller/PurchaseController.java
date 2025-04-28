package com.ohgiraffers.warehousemanagement.purchases.controller;


import com.ohgiraffers.warehousemanagement.purchases.model.dto.PurchaseDTO;

import com.ohgiraffers.warehousemanagement.purchases.model.entity.PurchaseStatus;
import com.ohgiraffers.warehousemanagement.purchases.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/purchases")
@Validated
public class PurchaseController {
    private PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;

    }


    /*
    메인, 전체조회 화면 , 발주대기 화면, 발주생성 화면, 발주 수정 화면
     */
     
    // 메인 페이지
    @GetMapping({"", "/"})
    public String mainPage(Model model) {
        List<PurchaseDTO> purchases = purchaseService.getAllPurchases();
        model.addAttribute("purchases", purchases);
        model.addAttribute("pageTitle", "발주 관리");
        model.addAttribute("cardTitle", "발주 목록");
        model.addAttribute("cardDescription", "모든 발주 내역을 확인하고 관리할 수 있습니다.");
        
        return "purchases";
    }


    // 전체조회
    @GetMapping("/list")
    public String getAllPurchases(Model model) {
        List<PurchaseDTO> purchases = purchaseService.getAllPurchases();
        model.addAttribute("purchases", purchases);
        model.addAttribute("pageTitle", "발주 관리");
        model.addAttribute("cardTitle", "발주 목록");
        model.addAttribute("cardDescription", "모든 발주 내역을 확인하고 관리할 수 있습니다.");
        
        return "purchases"; // 발주 목록 템플릿 직접 호출
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
        
        return "purchases";
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
        
        return "purchases";
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
        
        return "purchases"; // 템플릿 경로 수정
    }





    //단일조회
    @GetMapping("/{id}")
    public String getPurchaseById(@PathVariable Integer id, Model model) {
        PurchaseDTO purchaseDTO = purchaseService.getPurchaseById(id);
        model.addAttribute("selectedPurchase", purchaseDTO);
        model.addAttribute("pageTitle", "발주 상세 정보");
        model.addAttribute("cardTitle", "발주 목록");
        model.addAttribute("cardDescription", "발주 ID: " + id + "의 상세 정보를 확인할 수 있습니다.");
        
        // 총 금액 계산
//        if (purchaseDTO != null && purchaseDTO.getPurchaseItems() != null) {
//            double totalAmount = purchaseDTO.getPurchaseItems().stream()
//                .mapToDouble(item -> item.getPrice() * item.getQuantity())
//                .sum();
//            model.addAttribute("totalAmount", totalAmount);
//        }
//
        return "purchases"; // 경로를 purchases 템플릿으로 직접 이동
    }




    //발주 생성
    @GetMapping("/create")
    public String createPurchase(Model model) {
        model.addAttribute("purchaseDTO", new PurchaseDTO());
        model.addAttribute("pageTitle", "발주 생성");
        model.addAttribute("cardTitle", "신규 발주 등록");
        model.addAttribute("cardDescription", "새로운 발주를 등록할 수 있습니다.");

        return "create"; // 발주 생성 템플릿 경로
    }


    @PostMapping("/create")
    public String createPurchase(@Validated @ModelAttribute PurchaseDTO purchaseDTO, RedirectAttributes rdtat) {

        PurchaseDTO savedDTO = purchaseService.createPurchase(purchaseDTO);
        if (savedDTO != null) {
            rdtat.addFlashAttribute("salesDTO", savedDTO);
            rdtat.addFlashAttribute("message", "발주가 등록되었습니다.");
        } else {
            rdtat.addFlashAttribute("message","발주 등록에 실패하였습니다. 다시 시도해주세요.");
        }
        return "redirect:/purchases";
    }




    //발주 수정
    @GetMapping("/update/{id}")
    public String updatePurchase(@PathVariable Integer id, Model model) {
        PurchaseDTO purchaseDTO = purchaseService.getPurchaseById(id);
        model.addAttribute("purchaseDTO", purchaseDTO);
        model.addAttribute("pageTitle", "발주 수정");
        model.addAttribute("cardTitle", "발주 정보 수정");
        model.addAttribute("cardDescription", "발주 정보를 수정할 수 있습니다.");

        return "purchase-update"; // 발주 수정 템플릿 경로
    }

    @PostMapping("/update/{id}")
    public String updatePurchase(Model model,Integer id,@Validated @ModelAttribute PurchaseDTO purchaseDTO) {
         PurchaseDTO purchase = purchaseService.updatePurchase(purchaseDTO,id);
        return "redirect:/purchases";
    }

    //발주 삭제
    // 관리자만 삭제로 변경가능
    @PostMapping("/delete/{id}")
    public String deletePurchase(Model model,@PathVariable Integer id,RedirectAttributes rdtat) {
        purchaseService.deletedpurchase(id);
        return "redirect:/purchases";
    }

    //발주 완료(발주완료)
    // 관리자만 발주완료로 변경가능
    @PostMapping("/complete/{id}")
    public String completePurchase(@PathVariable Integer id,RedirectAttributes rdtat,@ModelAttribute PurchaseDTO purchaseDTO) {
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
    
    // 입고 처리 페이지 이동
    @GetMapping("/receive/{id}")
    public String receivePurchase(@PathVariable Integer id, Model model) {
        PurchaseDTO purchaseDTO = purchaseService.getPurchaseById(id);
        model.addAttribute("selectedPurchase", purchaseDTO);
        model.addAttribute("pageTitle", "발주 입고 처리");
        model.addAttribute("cardTitle", "발주 입고 처리");
        model.addAttribute("cardDescription", "발주 상품의 입고 처리를 진행할 수 있습니다.");
        
        return "purchase-receive"; // 입고 처리 템플릿
    }
    
    // 입고 처리 실행
    @PostMapping("/receive/{id}")
    public String processReceive(@PathVariable Integer id, RedirectAttributes rdtat) {
        boolean receiveResult = purchaseService.completePurchase(id); // 기존 완료 메서드 재활용
        
        if (receiveResult) {
            rdtat.addFlashAttribute("message", "발주 입고 처리가 완료되었습니다.");
        } else {
            rdtat.addFlashAttribute("message", "발주 입고 처리에 실패했습니다.");
        }
        
        return "redirect:/purchases";
    }


}
