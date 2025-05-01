package com.ohgiraffers.warehousemanagement.wms.inspection.controller;

import com.ohgiraffers.warehousemanagement.wms.auth.model.AuthDetails;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.dto.request.InspectionRequestDTO;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.dto.response.InspectionResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.dto.response.SearchResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.inspection.service.InspectionServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
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
    public String getAllInspection(@RequestParam(required = false) String inspectionType,
                                   @RequestParam(required = false) String searchType,
                                   @RequestParam(required = false) String search,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   Model model) {
        log.info("GET : /inspections");
        log.info("inspectionType : {}", inspectionType);
        log.info("searchType : {}", searchType);
        log.info("search : {}", search);
        Page<InspectionResponseDTO> inspectionList;
        SearchResponseDTO param = new SearchResponseDTO(inspectionType, searchType, search);

        if (inspectionType != null && !inspectionType.trim().isEmpty()) {
                inspectionList = inspectionServiceImpl.getAllInspectionByTag(param, page, size);
        } else {
                inspectionList = inspectionServiceImpl.getAllInspection(param, page, size);
        }
        model.addAttribute("inspectionList", inspectionList);
        model.addAttribute("param", param);
        return "inspections/inspections";
    }

    // 검수 추가 페이지
    @GetMapping("/create")
    public String create() {
        return "inspections/create";
    }

    // 검수 등록
    @PostMapping("/create")
    public String createInspection(Authentication authentication, @Validated InspectionRequestDTO requestDTO, RedirectAttributes ra) {
        log.info("POST : /inspections");
        log.info("inspectionRequestDTO : {}", requestDTO);

        AuthDetails authDetails = (AuthDetails) authentication.getPrincipal();
        Long userId = authDetails.getUserId();
        requestDTO.setUserId(userId);

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
    public String getInspectionById(@PathVariable Long inspectionId, Model model) {
        log.info("GET : /inspections/{}", inspectionId);

        InspectionResponseDTO responseDTO = inspectionServiceImpl.getInspectionById(inspectionId);

        model.addAttribute("inspection", responseDTO);

        return "inspections/detail";
    }

    // 검수 태그 및 태그 아이디로 검색
    @GetMapping("/{type}/{typeId}")
    public String getInspectionByTypeAndTypeId(@PathVariable String type, @PathVariable Long typeId, Model model) {
        log.info("GET : /inspections/{}/{}", type, typeId);

        InspectionResponseDTO responseDTO = inspectionServiceImpl.getInspectionByTagAndTagId(type, typeId);

        model.addAttribute("inspection", responseDTO);

        return "inspections/inspections";
    }

    // 검수 수정 페이지 보기
    @GetMapping("/{inspectionId}/edit")
    public String updateInspection(@PathVariable Long inspectionId, Model model) {
        log.info("GET : /inspections/edit/{}", inspectionId);

        InspectionResponseDTO responseDTO = inspectionServiceImpl.getInspectionById(inspectionId);

        model.addAttribute("inspection", responseDTO);

        return "inspections/update";
    }

    // 검수 수정하기
    @PostMapping("/{inspectionId}/edit")
    public String updateInspection(@PathVariable long inspectionId, @Validated InspectionRequestDTO requestDTO, RedirectAttributes ra) {
        log.info("POST : /inspections/{}", inspectionId);
        log.info("inspectionRequestDTO : {}", requestDTO);

        InspectionResponseDTO inspection = inspectionServiceImpl.updateInspection(inspectionId, requestDTO);

        if (inspection == null) {
            ra.addFlashAttribute("message", "검수 수정에 실패했습니다!");
        } else {
            ra.addFlashAttribute("message", "검수 수정에 성공했습니다!");
        }

        return "redirect:/inspections/" + inspectionId;
    }

    // 검수 삭제하기
    @PostMapping("/{inspectionId}/delete")
    public String deleteInspection(Authentication authentication, @PathVariable Long inspectionId, RedirectAttributes ra) {
        log.info("DELETE : /inspections/{}/delete", inspectionId);

        AuthDetails authDetails = (AuthDetails) authentication.getPrincipal();
        String userRole = authDetails.getUserRole();
        if(userRole.equals("관리자") || userRole.equals("매니저")) {
            inspectionServiceImpl.deleteInspection(inspectionId);

            ra.addFlashAttribute("message", inspectionId + "가 삭제되었습니다.");
        } else {
            ra.addFlashAttribute("message", "삭제 권한이 없습니다!");
        }

        return "redirect:/inspections";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String illegalArgumentException(Model model, IllegalArgumentException e) {
        log.error(e.getMessage());
        model.addAttribute("message", e.getMessage());
        return "inspections/error";
    }
}
