package com.ohgiraffers.warehousemanagement.wms.returning.controller;


import com.ohgiraffers.warehousemanagement.wms.returning.model.DTO.ReturnStorageDTO;
import com.ohgiraffers.warehousemanagement.wms.returning.model.entity.ReturnStorage;
import com.ohgiraffers.warehousemanagement.wms.returning.service.ReturnStorageService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/returns/inbound/list")
@Validated

public class ReturnStorageController {

    private final ReturnStorageService returnStorageService;

    @Autowired
    public ReturnStorageController(ReturnStorageService returnStorageService) {
        this.returnStorageService = returnStorageService;
    }
    /*전제 조회*/
    @GetMapping
    public ModelAndView getAllReturns(ModelAndView mv){
        List<ReturnStorageDTO> returnLists = returnStorageService.getAllReturns();

        if(returnLists != null){
            mv.addObject("returnLists",returnLists);
            mv.setViewName("returns/inbound/inbound");
        } else {
            mv.addObject("returnLists",new ArrayList<>());
        }
        return mv;
    }



    //등록화면
    @GetMapping("/create")
    public String registView(){
        return "returns/inbound/create";
    }

    @PostMapping("/create")
    public String createReturns(@Valid @ModelAttribute ReturnStorageDTO returnStorageDTO, RedirectAttributes rdtat) {
        int returnId = returnStorageService.createReturns(returnStorageDTO);
        String resultUrl = null;

        if(returnId > 0) {
            rdtat.addFlashAttribute("returnDTO", returnId);
            rdtat.addFlashAttribute("message", "반품서가 등록되었습니다.");
            resultUrl = "redirect:/returns/inbound/list"; // 성공시 홈화면
        }else{
            rdtat.addFlashAttribute("message","반품서 등록에 실패하였습니다. 다시 시도해주세요");
            resultUrl = "redirect:/returns/inbound/create";
        }
        return resultUrl;
    }

    /*상세조회*/
    @GetMapping("/{returnStorageId}")
    public ModelAndView getReturnsById(@PathVariable(name = "returnStorageId") Integer returnStorageId, ModelAndView mv, RedirectAttributes rdtat) {
        ReturnStorageDTO findDTO = returnStorageService.getReturnsById(returnStorageId);
        if(findDTO != null) {
            mv.addObject("returnsStorage", findDTO);
            mv.setViewName("returns/inbound/detail"); //입고에 관한 상세 수정
        }else {
            rdtat.addFlashAttribute("message","반품 데이터를 찾을 수 없습니다.");
            mv.setViewName("redirect:/returns/inboud/list");
        }
        return mv;
    }

//    /*삭제*/
//    @GetMapping("/{returnStorageId}/delete")
//    public String deleteReturns(@PathVariable(name = "returnStorageId")Integer returnStorageId,RedirectAttributes rdtat)
//    {
//        boolean isDeleted = returnStorageService.deleteReturns(returnStorageId);
//
//        if(isDeleted){
//            return "redirect:/returns/inbound/list"; //삭제에 성공 -> 홈화면으로
//        }else{
//            rdtat.addFlashAttribute("errorMessage","삭제에 실패했습니다. 다시 시도해주세요.");
//            return "redirect:/returns/inbound/"+returnStorageId;
//        }
//    }

}
