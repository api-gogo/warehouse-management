package com.ohgiraffers.warehousemanagement.wms.inspection.controller;

import com.ohgiraffers.warehousemanagement.wms.inspection.model.Inspection;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.dto.request.InspectionRequestDTO;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.dto.response.InspectionResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.inspection.service.InspectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/inspection")
public class InspectionController {
    private static final Logger log = LoggerFactory.getLogger(InspectionController.class);
    private final InspectionService inspectionService;

    @Autowired
    public InspectionController(InspectionService inspectionService) {
        this.inspectionService = inspectionService;
    }

    // 전체 검수 목록 및 검수 타입 검색(검수 메인 페이지)
    @GetMapping
    public String getAllInspection(@RequestParam(required = false) String type, Model model) {
        log.info("GET : /inspection");
        log.info("type : {}", type);
        List<Inspection> inspectionList = new ArrayList<>();

        if (type != null) {
            inspectionList = inspectionService.getAllInspectionByTag(type);
        } else {
            inspectionList = inspectionService.getAllInspection();
        }

        if (inspectionList.isEmpty()) {
            model.addAttribute("inspectionList", "검수 목록이 없습니다.");
        } else {
            model.addAttribute("inspectionList", inspectionList);
        }
        return "inspection/inspection";
    }

    // 검수 등록
    @PostMapping
    public String createInspection(@Validated @RequestBody InspectionRequestDTO requestDTO, RedirectAttributes ra) {
        log.info("POST : /inspection");
        log.info("inspectionRequestDTO : {}", requestDTO);

        InspectionResponseDTO responseDTO = inspectionService.createInspection(requestDTO);

        if (responseDTO == null) {
            ra.addFlashAttribute("message", "검수 등록에 실패했습니다!");
        } else {
            ra.addFlashAttribute("message", "검수 등록에 성공했습니다!");
        }

        return "redirect:/inspection";
    }

    // 검수 상세 보기
    @GetMapping("/{inspectionId}")
    public String getInspectionById(@PathVariable int inspectionId, Model model) {
        log.info("GET : /inspection/{}", inspectionId);

        InspectionResponseDTO responseDTO = inspectionService.getInspectionById(inspectionId);

        model.addAttribute("inspection", responseDTO);

        return "inspection/detail";
    }

    // 검수 수정 페이지 보기
    @GetMapping("/update/{inspectionId}")
    public String updateInspection(@PathVariable int inspectionId, Model model) {
        log.info("GET : /inspection/update/{}", inspectionId);

        InspectionResponseDTO responseDTO = inspectionService.getInspectionById(inspectionId);

        model.addAttribute("inspection", responseDTO);

        return "inspection/update";
    }

    // 검수 수정하기
    @PostMapping("/update/{inspectionId}")
    public String updateInspection(@PathVariable int inspectionId, @Validated InspectionRequestDTO requestDTO, RedirectAttributes ra) {
        log.info("POST : /inspection/update/{}", inspectionId);
        log.info("inspectionRequestDTO : {}", requestDTO);

        InspectionResponseDTO inspection = inspectionService.updateInspection(inspectionId, requestDTO);

        if (inspection == null) {
            ra.addFlashAttribute("message", "검수 수정에 실패했습니다!");
        } else {
            ra.addFlashAttribute("message", "검수 수정에 성공했습니다!");
        }

        return "redirect:/inspection/" + inspectionId;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String illegalArgumentException(Model model, IllegalArgumentException e) {
        log.error(e.getMessage());
        model.addAttribute("message", e.getMessage());
        return "inspection/error";
    }
}
