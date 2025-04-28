package com.ohgiraffers.warehousemanagement.wms.inspection.controller;

import com.ohgiraffers.warehousemanagement.wms.inspection.model.entity.Inspection;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.dto.request.InspectionRequestDTO;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.dto.response.InspectionResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.inspection.service.InspectionServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/inspections")
public class InspectionController {
    private static final Logger log = LoggerFactory.getLogger(InspectionController.class);
    private final InspectionServiceImpl inspectionServiceImpl;

    @Autowired
    public InspectionController(InspectionServiceImpl inspectionServiceImpl) {
        this.inspectionServiceImpl = inspectionServiceImpl;
    }

    // 전체 검수 목록 및 검수 타입 검색(검수 메인 페이지)
    @GetMapping
    public String getAllInspection(@RequestParam(required = false) String type,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   Model model) {
        log.info("GET : /inspections");
        log.info("type : {}", type);
        Page<Inspection> inspectionList;

        if (type != null) {
            inspectionList = inspectionServiceImpl.getAllInspectionByTag(type, page, size);
        } else {
            inspectionList = inspectionServiceImpl.getAllInspection(page, size);
        }

        if (inspectionList.getContent().isEmpty()) {
            model.addAttribute("inspectionList", "검수 목록이 없습니다.");
        } else {
            model.addAttribute("inspectionList", inspectionList);
        }
        return "inspections/inspection";
    }

    // 검수 등록
    @PostMapping
    public String createInspection(@Validated @RequestBody InspectionRequestDTO requestDTO, RedirectAttributes ra) {
        log.info("POST : /inspections");
        log.info("inspectionRequestDTO : {}", requestDTO);

        InspectionResponseDTO responseDTO = inspectionServiceImpl.createInspection(requestDTO);

        if (responseDTO == null) {
            ra.addFlashAttribute("message", "검수 등록에 실패했습니다!");
        } else {
            ra.addFlashAttribute("message", "검수 등록에 성공했습니다!");
        }

        return "redirect:/inspections";
    }

    // 검수 상세 보기
    @GetMapping("/{inspectionId}")
    public String getInspectionById(@PathVariable int inspectionId, Model model) {
        log.info("GET : /inspections/{}", inspectionId);

        InspectionResponseDTO responseDTO = inspectionServiceImpl.getInspectionById(inspectionId);

        model.addAttribute("inspection", responseDTO);

        return "inspections/detail";
    }

    // 검수 수정 페이지 보기
    @GetMapping("/update/{inspectionId}")
    public String updateInspection(@PathVariable int inspectionId, Model model) {
        log.info("GET : /inspections/update/{}", inspectionId);

        InspectionResponseDTO responseDTO = inspectionServiceImpl.getInspectionById(inspectionId);

        model.addAttribute("inspection", responseDTO);

        return "inspections/update";
    }

    // 검수 수정하기
    @PostMapping("/update/{inspectionId}")
    public String updateInspection(@PathVariable int inspectionId, @Validated InspectionRequestDTO requestDTO, RedirectAttributes ra) {
        log.info("POST : /inspections/update/{}", inspectionId);
        log.info("inspectionRequestDTO : {}", requestDTO);

        InspectionResponseDTO inspection = inspectionServiceImpl.updateInspection(inspectionId, requestDTO);

        if (inspection == null) {
            ra.addFlashAttribute("message", "검수 수정에 실패했습니다!");
        } else {
            ra.addFlashAttribute("message", "검수 수정에 성공했습니다!");
        }

        return "redirect:/inspections/" + inspectionId;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String illegalArgumentException(Model model, IllegalArgumentException e) {
        log.error(e.getMessage());
        model.addAttribute("message", e.getMessage());
        return "inspections/error";
    }
}
