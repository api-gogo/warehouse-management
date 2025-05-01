package com.ohgiraffers.warehousemanagement.wms.returning.controller;

import com.ohgiraffers.warehousemanagement.wms.returning.ReturnShipmentStatus;
import com.ohgiraffers.warehousemanagement.wms.returning.ReturningShipmentCause;
import com.ohgiraffers.warehousemanagement.wms.returning.model.DTO.ReturnShipmentDTO;
import com.ohgiraffers.warehousemanagement.wms.returning.service.ReturnShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/returns")
public class ReturnHomeController {

    private final ReturnShipmentService returnShipmentService;

    @Autowired
    public ReturnHomeController(ReturnShipmentService returnShipmentService) {
        this.returnShipmentService = returnShipmentService;
    }

    @GetMapping
    public ModelAndView showReturnsHome() {
        ModelAndView mv = new ModelAndView("returns/home");
        
        // 모든 반품 데이터 가져오기
        List<ReturnShipmentDTO> allReturns = returnShipmentService.getAllReturns();
        
        // 현재 시스템에는 출고 반품만 있음
        // 모든 반품을 출고 반품으로 처리
        List<ReturnShipmentDTO> inboundReturns = new ArrayList<>(); // 입고 반품은 없음
        List<ReturnShipmentDTO> outboundReturns = new ArrayList<>();
        
        if (allReturns != null && !allReturns.isEmpty()) {
            // 모든 반품을 출고 반품으로 분류
            outboundReturns.addAll(allReturns);
        }
        
        // 입고, 출고 반품 개수
        int inboundReturnsCount = inboundReturns.size();
        int outboundReturnsCount = outboundReturns.size();
        
        // 완료된 반품 개수 (RETURN_COMPLETED 상태인 반품)
        long completedReturnsCount = allReturns.stream()
            .filter(r -> r.getReturnShipmentStatus() == ReturnShipmentStatus.RETURN_COMPLETED)
            .count();
        
        // 상태별 개수 계산
        long inboundWaitingCount = inboundReturns.stream()
            .filter(r -> r.getReturnShipmentStatus() == ReturnShipmentStatus.RETURN_WAITING)
            .count();
        
        long inboundApprovedCount = inboundReturns.stream()
            .filter(r -> r.getReturnShipmentStatus() == ReturnShipmentStatus.RETURN_APPROVED)
            .count();
        
        long inboundRejectedCount = inboundReturns.stream()
            .filter(r -> r.getReturnShipmentStatus() == ReturnShipmentStatus.RETURN_REJECTED)
            .count();
        
        long inboundCompletedCount = inboundReturns.stream()
            .filter(r -> r.getReturnShipmentStatus() == ReturnShipmentStatus.RETURN_COMPLETED)
            .count();
        
        long outboundWaitingCount = outboundReturns.stream()
            .filter(r -> r.getReturnShipmentStatus() == ReturnShipmentStatus.RETURN_WAITING)
            .count();
        
        long outboundApprovedCount = outboundReturns.stream()
            .filter(r -> r.getReturnShipmentStatus() == ReturnShipmentStatus.RETURN_APPROVED)
            .count();
        
        long outboundRejectedCount = outboundReturns.stream()
            .filter(r -> r.getReturnShipmentStatus() == ReturnShipmentStatus.RETURN_REJECTED)
            .count();
        
        long outboundCompletedCount = outboundReturns.stream()
            .filter(r -> r.getReturnShipmentStatus() == ReturnShipmentStatus.RETURN_COMPLETED)
            .count();
        
        long totalWaitingCount = inboundWaitingCount + outboundWaitingCount;
        long totalApprovedCount = inboundApprovedCount + outboundApprovedCount;
        long totalRejectedCount = inboundRejectedCount + outboundRejectedCount;
        long totalCompletedCount = inboundCompletedCount + outboundCompletedCount;
        
        // 최근 반품 내역 (최대 5개, 생성일 기준 내림차순 정렬)
        List<Map<String, Object>> recentReturns = new ArrayList<>();
        
        if (allReturns != null && !allReturns.isEmpty()) {
            // 생성일 기준 내림차순 정렬
            allReturns.sort((a, b) -> 
                b.getReturnShipmentCreatedAt().compareTo(a.getReturnShipmentCreatedAt()));
            
            // 최대 5개까지만 가져옴
            int count = Math.min(allReturns.size(), 5);
            
            for (int i = 0; i < count; i++) {
                ReturnShipmentDTO returnDTO = allReturns.get(i);
                Map<String, Object> returnMap = new HashMap<>();
                
                returnMap.put("returnId", returnDTO.getReturnShipmentId());
                // 모든 항목을 출고 반품으로 표시
                returnMap.put("type", "OUTBOUND");
                
                // 매장 이름 설정 (예시)
                int storeId = returnDTO.getStoreId();
                String storeName = "알 수 없음";
                if (storeId == 1) storeName = "서울 매장";
                else if (storeId == 2) storeName = "대구 매장";
                else if (storeId == 3) storeName = "부산 매장";
                else if (storeId == 4) storeName = "인천 매장";
                returnMap.put("storeName", storeName);
                
                returnMap.put("createdAt", returnDTO.getReturnShipmentCreatedAt());
                returnMap.put("status", returnDTO.getReturnShipmentStatus().name());
                
                recentReturns.add(returnMap);
            }
        }
        
        // 반품 사유 통계 - 실제 데이터 집계를 시도
        // ReturnShipmentItem에서 사유 데이터를 집계해야 하지만 현재는 간단히 처리
        long expiredCount = 0;
        long damagedCount = 0;
        long wrongItemCount = 0;
        long changedMindCount = 0;
        
        // 실제 데이터에 기반한 반품 사유 집계
        // 각 반품의 항목에서 returnShipmentContent를 확인해야 하지만
        // ReturnShipmentDTO에 항목 정보가 포함되어 있다면 집계 가능
        for (ReturnShipmentDTO dto : allReturns) {
            if (dto.getReturnShipmentContent() != null) {
                for (ReturningShipmentCause cause : dto.getReturnShipmentContent()) {
                    if (cause == ReturningShipmentCause.EXPIRED) {
                        expiredCount++;
                    } else if (cause == ReturningShipmentCause.DAMAGED_OR_DEFECTIVE) {
                        damagedCount++;
                    }
                }
            }
        }
        
        // 모든 데이터를 ModelAndView에 추가
        mv.addObject("inboundReturnsCount", inboundReturnsCount);
        mv.addObject("outboundReturnsCount", outboundReturnsCount);
        mv.addObject("completedReturnsCount", completedReturnsCount);
        
        mv.addObject("inboundWaitingCount", inboundWaitingCount);
        mv.addObject("inboundApprovedCount", inboundApprovedCount);
        mv.addObject("inboundRejectedCount", inboundRejectedCount);
        mv.addObject("inboundCompletedCount", inboundCompletedCount);
        
        mv.addObject("outboundWaitingCount", outboundWaitingCount);
        mv.addObject("outboundApprovedCount", outboundApprovedCount);
        mv.addObject("outboundRejectedCount", outboundRejectedCount);
        mv.addObject("outboundCompletedCount", outboundCompletedCount);
        
        mv.addObject("totalWaitingCount", totalWaitingCount);
        mv.addObject("totalApprovedCount", totalApprovedCount);
        mv.addObject("totalRejectedCount", totalRejectedCount);
        mv.addObject("totalCompletedCount", totalCompletedCount);
        
        mv.addObject("recentReturns", recentReturns);
        
        // 반품 사유 통계
        mv.addObject("expiredCount", expiredCount);
        mv.addObject("damagedCount", damagedCount);
        mv.addObject("wrongItemCount", wrongItemCount);
        mv.addObject("changedMindCount", changedMindCount);
        
        return mv;
    }

    @GetMapping("/completed")
    public ModelAndView showCompletedReturns() {
        ModelAndView mv = new ModelAndView("returns/completed");
        
        try {
            // 모든 반품 데이터 가져오기
            List<ReturnShipmentDTO> allReturns = returnShipmentService.getAllReturns();
            System.out.println("Fetched returns count: " + (allReturns != null ? allReturns.size() : "null"));
            
            // RETURN_COMPLETED 상태인 반품만 필터링
            List<Map<String, Object>> completedReturns = new ArrayList<>();
            
            if (allReturns != null && !allReturns.isEmpty()) {
                // 필터링하기 전 상태 확인
                System.out.println("Status counts:");
                for (ReturnShipmentStatus status : ReturnShipmentStatus.values()) {
                    long count = allReturns.stream()
                        .filter(r -> r.getReturnShipmentStatus() == status)
                        .count();
                    System.out.println(" - " + status + ": " + count);
                }
                
                // 완료 상태인 반품만 필터링
                allReturns.stream()
                    .filter(r -> r.getReturnShipmentStatus() == ReturnShipmentStatus.RETURN_COMPLETED)
                    .forEach(returnDTO -> {
                        Map<String, Object> returnMap = new HashMap<>();
                        returnMap.put("returnId", returnDTO.getReturnShipmentId());
                        returnMap.put("type", "OUTBOUND"); // 모든 항목을 출고 반품으로 표시
                        
                        int storeId = returnDTO.getStoreId();
                        String storeName = "알 수 없음";
                        if (storeId == 1) storeName = "서울 매장";
                        else if (storeId == 2) storeName = "대구 매장";
                        else if (storeId == 3) storeName = "부산 매장";
                        else if (storeId == 4) storeName = "인천 매장";
                        returnMap.put("storeName", storeName);
                        
                        returnMap.put("createdAt", returnDTO.getReturnShipmentCreatedAt());
                        returnMap.put("updatedAt", returnDTO.getReturnShipmentUpdatedAt());
                        returnMap.put("status", returnDTO.getReturnShipmentStatus().name());
                        
                        completedReturns.add(returnMap);
                    });
            }
            
            System.out.println("Completed returns count: " + completedReturns.size());
            
            // 데이터가 없을 경우 테스트용 예시 데이터 추가
            if (completedReturns.isEmpty()) {
                System.out.println("Adding sample data for testing...");
                Map<String, Object> sampleData = new HashMap<>();
                sampleData.put("returnId", 9999);
                sampleData.put("type", "OUTBOUND");
                sampleData.put("storeName", "테스트 매장");
                sampleData.put("createdAt", LocalDateTime.now().minusDays(5));
                sampleData.put("updatedAt", LocalDateTime.now().minusDays(2));
                sampleData.put("status", "RETURN_COMPLETED");
                completedReturns.add(sampleData);
            }
            
            mv.addObject("completedReturns", completedReturns);
        } catch (Exception e) {
            System.err.println("Error in showCompletedReturns: " + e.getMessage());
            e.printStackTrace();
            mv.addObject("error", "데이터를 불러오는 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        return mv;
    }

    @GetMapping("/inbound") //입고반품 전체조회
    public String showInboundReturns() {
        return "returns/inbound/inbound";
    }

    @GetMapping("/outbound") //출고반품 전체조회
    public String showOutboundReturns() {
        return "redirect:/returns/outbound/list";
    }
}