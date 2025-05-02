package com.ohgiraffers.warehousemanagement.wms.returning.controller;

import com.ohgiraffers.warehousemanagement.wms.returning.ReturnShipmentStatus;
import com.ohgiraffers.warehousemanagement.wms.returning.ReturningShipmentCause;
import com.ohgiraffers.warehousemanagement.wms.returning.model.DTO.ReturnShipmentDTO;
import com.ohgiraffers.warehousemanagement.wms.returning.service.ReturnShipmentService;
import com.ohgiraffers.warehousemanagement.wms.store.model.entity.Store; // Import Store entity
import com.ohgiraffers.warehousemanagement.wms.store.repository.StoreRepository; // Import StoreRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam; // Added for filtering
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate; // Added for date filtering
import java.time.LocalDateTime;
import java.time.LocalTime; // Added for date filtering
import java.util.*;
import java.util.function.Function; // Added for map creation
import java.util.stream.Collectors; // Added for stream operations

@Controller
@RequestMapping("/returns")
public class ReturnHomeController {

    private final ReturnShipmentService returnShipmentService;
    private final StoreRepository storeRepository; // Added StoreRepository dependency

    @Autowired
    public ReturnHomeController(ReturnShipmentService returnShipmentService,
                                StoreRepository storeRepository /* Added to constructor */) {
        this.returnShipmentService = returnShipmentService;
        this.storeRepository = storeRepository; // Initialize repository
    }

    // Helper method to get Store ID -> Name Map
    private Map<Integer, String> getStoreNameMap() {
        return storeRepository.findAll().stream()
                .collect(Collectors.toMap(Store::getStoreId, Store::getStoreName, (existing, replacement) -> existing)); // Handle potential duplicate keys if any
    }


    @GetMapping
    public ModelAndView showReturnsHome() {
        ModelAndView mv = new ModelAndView("returns/home");
        Map<Integer, String> storeNameMap = getStoreNameMap(); // Get store names

        // Fetch all return data (assuming only outbound for now)
        List<ReturnShipmentDTO> allReturns = returnShipmentService.getAllReturns();
        if (allReturns == null) {
            allReturns = new ArrayList<>(); // Ensure list is not null
        }

        // --- Statistics Calculation (Mostly unchanged, uses allReturns) ---
        List<ReturnShipmentDTO> inboundReturns = new ArrayList<>(); // Keep distinction, though currently empty
        List<ReturnShipmentDTO> outboundReturns = new ArrayList<>(allReturns); // Assume all are outbound

        int inboundReturnsCount = inboundReturns.size();
        int outboundReturnsCount = outboundReturns.size();

        long completedReturnsCount = allReturns.stream()
                .filter(r -> r.getReturnShipmentStatus() == ReturnShipmentStatus.RETURN_COMPLETED)
                .count();

        // Status counts (calculated based on the current data structure)
        long inboundWaitingCount = 0; // Since inboundReturns is empty
        long inboundApprovedCount = 0;
        long inboundRejectedCount = 0;
        long inboundCompletedCount = 0;

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

        // --- Recent Returns (Refactored Store Name) ---
        List<Map<String, Object>> recentReturns = new ArrayList<>();
        allReturns.sort(Comparator.comparing(ReturnShipmentDTO::getReturnShipmentCreatedAt).reversed()); // Sort by creation date desc

        int count = Math.min(allReturns.size(), 5);
        for (int i = 0; i < count; i++) {
            ReturnShipmentDTO returnDTO = allReturns.get(i);
            Map<String, Object> returnMap = new HashMap<>();

            returnMap.put("returnId", returnDTO.getReturnShipmentId());
            returnMap.put("type", "OUTBOUND"); // Assuming outbound based on service used
            // Use map to get actual store name
            returnMap.put("storeName", storeNameMap.getOrDefault(returnDTO.getStoreId(), "ID: " + returnDTO.getStoreId()));
            returnMap.put("createdAt", returnDTO.getReturnShipmentCreatedAt());
            returnMap.put("status", returnDTO.getReturnShipmentStatus().name()); // Pass enum name for status

            recentReturns.add(returnMap);
        }

        // --- Reason Statistics (Unchanged logic, depends on DTO content) ---
        long expiredCount = 0;
        long damagedCount = 0;
        // Add counts for other reasons if they exist in ReturningShipmentCause enum and are used
        // long wrongItemCount = 0;
        // long changedMindCount = 0;

        for (ReturnShipmentDTO dto : allReturns) {
            if (dto.getReturnShipmentContent() != null) {
                expiredCount += dto.getReturnShipmentContent().stream()
                        .filter(cause -> cause == ReturningShipmentCause.EXPIRED)
                        .count();
                damagedCount += dto.getReturnShipmentContent().stream()
                        .filter(cause -> cause == ReturningShipmentCause.DAMAGED_OR_DEFECTIVE)
                        .count();
                // Add counting logic for other causes here
            }
        }

        // Add all data to ModelAndView
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

        mv.addObject("expiredCount", expiredCount);
        mv.addObject("damagedCount", damagedCount);
        // Add other reason counts
        // mv.addObject("wrongItemCount", wrongItemCount);
        // mv.addObject("changedMindCount", changedMindCount);

        return mv;
    }

    @GetMapping("/completed")
    public ModelAndView showCompletedReturns(
            // Add RequestParams for filtering
            @RequestParam(required = false) String type,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            ModelAndView mv) { // Inject ModelAndView directly

        mv.setViewName("returns/completed"); // Set view name early
        Map<Integer, String> storeNameMap = getStoreNameMap(); // Get store names

        try {
            // Fetch all returns (assuming only outbound for now)
            List<ReturnShipmentDTO> allReturns = returnShipmentService.getAllReturns();
            if (allReturns == null) {
                allReturns = new ArrayList<>(); // Ensure list is not null
            }

            // Filter for completed status and apply optional filters
            List<Map<String, Object>> completedReturns = allReturns.stream()
                    .filter(r -> r.getReturnShipmentStatus() == ReturnShipmentStatus.RETURN_COMPLETED)
                    // Apply type filter (currently only OUTBOUND exists from this service)
                    .filter(r -> type == null || type.isEmpty() || type.equalsIgnoreCase("OUTBOUND")) // Adapt if INBOUND is added
                    // Apply date filters
                    .filter(r -> startDate == null || !r.getReturnShipmentCreatedAt().toLocalDate().isBefore(startDate))
                    .filter(r -> endDate == null || !r.getReturnShipmentCreatedAt().toLocalDate().isAfter(endDate))
                    // Map to the structure needed by the view
                    .map(returnDTO -> {
                        Map<String, Object> returnMap = new HashMap<>();
                        returnMap.put("returnId", returnDTO.getReturnShipmentId());
                        returnMap.put("type", "OUTBOUND"); // Assuming outbound
                        // Use map for store name
                        returnMap.put("storeName", storeNameMap.getOrDefault(returnDTO.getStoreId(), "ID: " + returnDTO.getStoreId()));
                        returnMap.put("createdAt", returnDTO.getReturnShipmentCreatedAt());
                        returnMap.put("updatedAt", returnDTO.getReturnShipmentUpdatedAt()); // Completion date is often update date
                        returnMap.put("status", returnDTO.getReturnShipmentStatus().name());
                        return returnMap;
                    })
                    // Sort by completion date (updatedAt) descending
                    .sorted((m1, m2) -> ((LocalDateTime)m2.get("updatedAt")).compareTo((LocalDateTime)m1.get("updatedAt")))
                    .collect(Collectors.toList());

            mv.addObject("completedReturns", completedReturns);
            // Pass filter values back to the view to keep them selected
            mv.addObject("selectedType", type);
            mv.addObject("selectedStartDate", startDate);
            mv.addObject("selectedEndDate", endDate);

        } catch (Exception e) {
            System.err.println("Error in showCompletedReturns: " + e.getMessage());
            e.printStackTrace();
            mv.addObject("error", "데이터를 불러오는 중 오류가 발생했습니다: " + e.getMessage());
            mv.addObject("completedReturns", new ArrayList<>()); // Ensure list exists even on error
        }

        return mv;
    }

    // Redirects remain the same
    @GetMapping("/inbound")
    public String showInboundReturns() {
        return "redirect:/returns/inbound/list"; // Assuming this path exists for inbound
    }

    @GetMapping("/outbound")
    public String showOutboundReturns() {
        return "redirect:/returns/outbound/list";
    }
}