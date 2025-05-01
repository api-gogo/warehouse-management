package com.ohgiraffers.warehousemanagement.wms.returning.controller;

import com.ohgiraffers.warehousemanagement.wms.returning.ReturnShipmentStatus;
import com.ohgiraffers.warehousemanagement.wms.returning.model.DTO.ReturnShipmentDTO;
import com.ohgiraffers.warehousemanagement.wms.returning.service.ReturnShipmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

/*출고반품*/
@Controller
@RequestMapping("/returns/outbound/list")
@Validated

public class ReturningController {

    //의존성 주입
    private final ReturnShipmentService returnShipmentService;

    @Autowired
    public ReturningController(ReturnShipmentService returnShipmentService) {
        this.returnShipmentService = returnShipmentService;
    }

    //(반품서) 전체 조회
    //getALlReturning메서드 호출해서 데이터들(List) 받아오기

    @GetMapping
    public ModelAndView getAllReturns(ModelAndView mv) {

        //서비스의 getALLReturning 호출
        List<ReturnShipmentDTO> returnLists = returnShipmentService.getAllReturns();

        if (returnLists != null) {
            mv.addObject("returnLists", returnLists);
            mv.setViewName("returns/outbound/outbound");
        } else {
            mv.addObject("returnLists", new ArrayList<>());
        }

        return mv;
    }

    //등록 화면 - 신규반품(뷰 화면 필요 - html필요)
    @GetMapping("/create")
    public String registView() {
        return "returns/outbound/create";
    } //returning/regist_return.html에 등록할 정보들 담기

    //등록 --> 등록일 달아주기, 리다이렉트로 다시 돌아가기..
    @PostMapping("/create")
    public String createReturns(@Valid @ModelAttribute ReturnShipmentDTO returnShipmentDTO, RedirectAttributes rdtat) {

        System.out.println("컨트롤러에서 데이터 넘어오는지: " + returnShipmentDTO);
        int returnShipmentId = returnShipmentService.createReturns(returnShipmentDTO);
        String resultUrl = null;

        if (returnShipmentId > 0) {
            rdtat.addFlashAttribute("returnshipmentDTO", returnShipmentId);
            rdtat.addFlashAttribute("message", "반품서가 등록되었습니다.");
            resultUrl = "redirect:/returns/outbound/list"; //성공시 홈화면
        } else {
            rdtat.addFlashAttribute("message", "반품서 등록에 실패하였습니다.");
            resultUrl = "redirect:/returns/outbound/create"; //+ returnShipmentId; 다시 등록화면?


        }
        return resultUrl;
    }
        // 출고기록ID(출고번호) 일치 확인 --> 외래키 : 출고기록ID
        // (반품서 상태 : [반품대기])
        //ㅁ 일치한다면 서비스로 출고기록ID 넘김
        //ㅁ 불일치 시 “다시 확인해달라” 예외처리
        //CreateReturning메서드


        //상세 조회
        //getReturningById메서드 호출 -> returnShipmentId를 대입해서 값 받아오기
        //반품번호를 기준으로 조회 -> 전체조회 중에 아이디가 일치하는 값으로 조회하기 ->id 값은 url에서 가져옴
        @GetMapping("/{returnShipmentId}")
        public ModelAndView getReturnsByID (@PathVariable(name = "returnShipmentId") Integer
        returnShipmentId, ModelAndView mv, RedirectAttributes rdtat){
            ReturnShipmentDTO returnShipmentDTO = returnShipmentService.getReturnsByID(returnShipmentId);
            //returnShipmentDTO.setReturnShipmentId(returnShipmentId); 서비스에서 이미 설정
            if (returnShipmentDTO != null) {
                mv.addObject("returns", returnShipmentDTO);
                mv.setViewName("returns/outbound/detail"); //상세 페이지로 이동
            } else {
                rdtat.addFlashAttribute("message", "반품 데이터를 찾을 수 없습니다.");
                mv.setViewName("redirect:/returns/outbound/list");//전체조회 페이지로 이동
            }
            //뷰네임 : detail, 경로 : returning/detail

            return mv;
        }


        //삭제 -> 삭제일 달아주기
        //반품번호(returnShipmentId)를 기준으로 삭제 deletebyid (논리적 삭제 사용 - isdelete의 상태값 변경 - 만약 isdelete가 0이라면 사라지는 상태)
        //삭제의 성공 여부 보여주고 다시 전체조회 페이지로 넘어가기 -> 리다이렉트
        //단순 페이지 이동이므로 모델의 이름과 속성을 담아줄 필요가 없을듯? -> ModelAndView 사용x
        //deleteReturning메서드
        @GetMapping("/{returnShipmentId}/delete")
        public String deleteReturns (@PathVariable(name = "returnShipmentId") Integer
        returnShipmentId, RedirectAttributes redirectAttributes){

            /*삭제 상태 변경 - > 논리적 삭제*/
            boolean isDeleted = returnShipmentService.deleteReturns(returnShipmentId);

            if (isDeleted) {
                return "redirect:/returns/outbound/list"; //삭제에 성공->홈화면으로
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "삭제에 실패했습니다. 다시 시도해주세요.");
                return "redirect:/returns/outbound/" + returnShipmentId;// 실패시 상세조회
            }
        }
        //실패해서 상세페이지로 돌아간다면 -> 예외처리를 해줘야됨
        //트라이 캐치 써서 성공하면 전체조회 , 실패하면 오류를 띄워주고 원래폼으로 리다이렉트

        //수정 -> 수정일 달아주기
        //상세 조회 페이지에서 수정 버튼을 누르면 수정할 수 있게
        //
        //조회 -> 받은 값 save로 덮어씌우기
        //updateReturning메서드

        //수정 화면 - html필요
        @GetMapping("/{return_shipment_id}/update")
        public ModelAndView updateReturnsById (@PathVariable("return_shipment_id") Integer
        returnShipmentId, ModelAndView mv, RedirectAttributes rdtat)
        {
            ReturnShipmentDTO rsDTO = returnShipmentService.getReturnsByID(returnShipmentId);

            if (rsDTO != null) {
                mv.addObject("ReturnShipmentDTO", rsDTO);
                mv.setViewName("returns/outbound/update_view");
            } else {
                rdtat.addFlashAttribute("message", "반품 데이터를 찾을 수 없습니다.");
                mv.setViewName("redirect:/returns/{return_shipment_id}/update");
            }

            return mv;
        }
        //수정 - 데이터를 받고 DTO로 저장 (서비스 클래스에서 DTO를 엔티티로 바꿔서 DB에 저장할 예정)
        //html 작성이후 @RequestParam 삭제하기..@@@@@@

        @PostMapping("/{return_shipment_id}/update")
        public String updateReturns (@PathVariable("return_shipment_id") Integer returnShipmentId,
                @Valid @ModelAttribute ReturnShipmentDTO returnShipmentDTO,
                RedirectAttributes rdtat){
            ReturnShipmentDTO updateDTO = returnShipmentService.updateReturns(returnShipmentId, returnShipmentDTO);
            String resultUrl = null;

            if (updateDTO != null) {
                rdtat.addFlashAttribute("returnShipmentDTO", updateDTO);
                rdtat.addFlashAttribute("message", "반품서를 수정했습니다.");
                resultUrl = "redirect:/returns/outbound/list";
            } else {
                rdtat.addFlashAttribute("message", "반품서 수정에 실패했습니다. 다시 시도해주세요");
                resultUrl = "redirect:/returns/outbound/list" + returnShipmentId;
            }
            return resultUrl;
        }

        @PostMapping("{return_shipment_id}/update/status/")
        public String updateStatusReturns (@PathVariable Integer returnShipmentId, @RequestParam ReturnShipmentStatus
        returnShipmentStatus, RedirectAttributes rdtat){
            boolean result = returnShipmentService.updateStatusReturns(returnShipmentId, returnShipmentStatus);
            String resultUrl = null;

            if (result) {
                rdtat.addFlashAttribute("message", "반품 상태가 변경되었습니다.");
                resultUrl = "redirect:/returns/outbound/list" ;
            } else {
                rdtat.addFlashAttribute("message", "상태변경에 실패했습니다. 다시 시도해주세요");
                resultUrl = "redirect:/returns/outbound/list/"+returnShipmentId;
            }
            return resultUrl;
        }
        //홈화면

        //비즈니스 로직 나중에 추가

    }

