package com.ohgiraffers.warehousemanagement.wms.returning.controller;

import com.ohgiraffers.warehousemanagement.wms.returning.model.DTO.ReturnShipmentDTO;
import com.ohgiraffers.warehousemanagement.wms.returning.model.DTO.ReturnStorageDTO;
import com.ohgiraffers.warehousemanagement.wms.returning.model.entity.ReturnStorage;
import com.ohgiraffers.warehousemanagement.wms.returning.service.ReturningStoreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/*입고반품*/

@Controller
@RequestMapping("/returns/inbound")
public class ReturningStoreController {

    private final ReturningStoreService returningStoreService;

    @Autowired
    public ReturningStoreController(ReturningStoreService returningStoreService) {
        this.returningStoreService = returningStoreService;
    }

    //전체 조회
    @GetMapping
    public ModelAndView getALlReturning(ModelAndView mv) {

        List<ReturnStorageDTO> rsDTOs = returningStoreService.getALlReturning();
        mv.addObject("findAll", rsDTOs);
        mv.setViewName("returns/inbound/returningList");

        return mv;
    }

    //등록화면
    @GetMapping("/regist")
    public String registView(){
        return "redirect:/returns/inbound/regist_return";
    }
    //등록 - pk값 사용x
    @PostMapping("/regist")
    public String CreateReturning(@ModelAttribute ReturnStorageDTO returnStorageDTO) {
        ReturnStorageDTO rsDTO = returningStoreService.CreateReturning(returnStorageDTO);

        return "redirect:/returns/inbound/"+rsDTO.getReturnStorageId();
    }

    //상세조회
    @GetMapping("/{return_storage_id}/detail")
    public ModelAndView getReturningById(@PathVariable("return_storage_id") Integer returnStorageId, ModelAndView mv) {
        ReturnStorageDTO returnStorageDTO = returningStoreService.getReturningById(returnStorageId);

        mv.addObject("detail", returnStorageDTO);
        mv.setViewName("returns/inbound/detail");

        return mv;
    }

    //삭제
    @GetMapping("/{return_storage_id}/delete")
    public String deleteReturning(@PathVariable("return_storage_id") Integer returnStorageId){

        return "redirect:/returns/inbound/"+returnStorageId;
    }

    //수정
    @PostMapping("/{return_storage_id}/update")
    public String updateReturning(@Valid ReturnStorageDTO returnStorageDTO,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "returns/inbound/update_view";
        }
        returningStoreService.updateReturning(returnStorageDTO);

        return "redirect:/returns/inbound/returningList";

    }
}
