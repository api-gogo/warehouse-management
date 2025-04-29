package com.ohgiraffers.warehousemanagement.wms.returning.controller;

import com.ohgiraffers.warehousemanagement.wms.returning.model.DTO.ReturnShipmentDTO;
import com.ohgiraffers.warehousemanagement.wms.returning.service.ReturningService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;
import java.util.List;

/*출고반품*/
@Controller
@RequestMapping("/returns/outbound")
public class ReturningController {

    //의존성 주입
    private final ReturningService returningService;

    @Autowired
    public ReturningController(ReturningService returningService) {
        this.returningService = returningService;
    }
    //(반품서) 전체 조회
    //getALlReturning메서드 호출해서 데이터들(List) 받아오기
    @GetMapping
    public ModelAndView getAllReturning(ModelAndView mv) {

       //서비스의 getALLReturning 호출
                 List<ReturnShipmentDTO> rsDTOs =  returningService.getAllReturning();
                 mv.addObject("findAll", rsDTOs);
                 mv.setViewName("returns/outbound/returningList");

        return mv;
    }

    //등록 화면 - 신규반품(뷰 화면 필요 - html필요)
    @GetMapping("/regist")
    public String registView(){
        return "returns/outbound/regist_return";
    } //returning/regist_return.html에 등록할 정보들 담기

    //등록 --> 등록일 달아주기, 리다이렉트로 다시 돌아가기..
    @PostMapping("/regist")
    public String createReturning(@Valid @ModelAttribute ReturnShipmentDTO returnShipmentDTO) {
        ReturnShipmentDTO rsDTO = returningService.createReturning(returnShipmentDTO); //담기만 한 상태

        return "redirect:/returns/outbound/"+rsDTO.getReturnShipmentId(); //흠..********** 로직도 추가
        //자동으로 returing/detail을 랜더링해서 해당id의 상세조회 주소로 다시 리다이렉트 해줌(작성된 양식들은 삭제)

    }

    // 출고기록ID(출고번호) 일치 확인 --> 외래키 : 출고기록ID
    // (반품서 상태 : [반품대기])
    //ㅁ 일치한다면 서비스로 출고기록ID 넘김
    //ㅁ 불일치 시 “다시 확인해달라” 예외처리
    //CreateReturning메서드


    //상세 조회
    //getReturningById메서드 호출 -> returnShipmentId를 대입해서 값 받아오기
    //반품번호를 기준으로 조회 -> 전체조회 중에 아이디가 일치하는 값으로 조회하기 ->id 값은 url에서 가져옴
    @GetMapping("/{return_shipment_id}/detail")
    public ModelAndView getReturningByID(@PathVariable("return_shipment_id") Integer returnShipmentId, ModelAndView mv) {
        ReturnShipmentDTO returnShipmentDTO = returningService.getReturningById(returnShipmentId);
        //returnShipmentDTO.setReturnShipmentId(returnShipmentId); 서비스에서 이미 설정

        mv.addObject("detail", returnShipmentDTO);
        mv.setViewName("returns/outbound/detail"); //뷰네임 : detail, 경로 : returning/detail

        return mv;
    }


    //삭제 -> 삭제일 달아주기
    //반품번호(returnShipmentId)를 기준으로 삭제 deletebyid (논리적 삭제 사용 - isdelete의 상태값 변경 - 만약 isdelete가 0이라면 사라지는 상태)
    //삭제의 성공 여부 보여주고 다시 전체조회 페이지로 넘어가기 -> 리다이렉트
    //단순 페이지 이동이므로 모델의 이름과 속성을 담아줄 필요가 없을듯? -> ModelAndView 사용x
    //deleteReturning메서드
    @GetMapping("/{return_shipment_id}/delete")
    public String deleteReturning(@PathVariable("return_shipment_id") Integer returnShipmentId, RedirectAttributes redirectAttributes) {

        /*삭제 상태 변경 - > 논리적 삭제*/
        boolean isDeleted = returningService.deleteReturning(returnShipmentId);

        if (isDeleted) {
               return "redirect:/returns/outbound"; //삭제에 성공->홈화면으로
           }else {
                redirectAttributes.addFlashAttribute("errorMessage","삭제에 실패했습니다. 다시 시도해주세요.");
               return "redirect:/returns/outbound/"+returnShipmentId;
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
    @GetMapping("/{return_shipment_id}/update_view")
    public ModelAndView updateReturningById(@PathVariable("return_shipment_id") Integer returnShipmentId, ModelAndView mv)
    {
            ReturnShipmentDTO returnShipmentDTO = returningService.getReturningById(returnShipmentId);
           // returnShipmentDTO.setReturnShipmentId(returnShipmentId);
            mv.addObject("detail", returnShipmentDTO);
            mv.setViewName("returns/outbound/update_view");
            return mv;
    }
    //수정 - 데이터를 받고 DTO로 저장 (서비스 클래스에서 DTO를 엔티티로 바꿔서 DB에 저장할 예정)
    //html 작성이후 @RequestParam 삭제하기..@@@@@@

    @PostMapping("/{return_shipment_id}/update")
    public String updateReturning(@PathVariable("return_shipment_id") Integer returnShipmentId,
                                  @Valid @ModelAttribute ReturnShipmentDTO returnShipmentDTO,
                                  BindingResult bindingResult,
                                  @RequestParam("shipment_id") Integer shipmentId,
                                  @RequestParam("store_id")Integer storeId ,
                                  @RequestParam("user_id") Integer userId ,
                                  @RequestParam("lot_number") String lotNumber ,
                                  @RequestParam("return_shipment_quantity") int returnShipmentQuantity ,
                                  @RequestParam("return_shipment_content") String returnShipmentContent ,
                                  @RequestParam("return_shipment_status") String returnShipmentStatus ,
                                  @RequestParam("return_shipment_created_at") LocalDateTime returnShipmentCreatedAt ,
                                  @RequestParam("return_shipment_deleted_at") LocalDateTime returnShipmentDeletedAt) {

        if(bindingResult.hasErrors()) {
            return "returns/outbound/update_view";
        }

        returnShipmentDTO.setReturnShipmentId(returnShipmentId);//--> 이거는 필요, html폼에서 작성하는건 아니니까
        returnShipmentDTO.setShipmentId(shipmentId);
        returnShipmentDTO.setStoreId(storeId);
        returnShipmentDTO.setUserId(userId);
        returnShipmentDTO.setLotNumber(lotNumber);
        returnShipmentDTO.setReturnShipmentQuantity(returnShipmentQuantity);
        returnShipmentDTO.setReturnShipmentContent(returnShipmentContent);
        returnShipmentDTO.setReturnShipmentStatus(returnShipmentStatus);
        returnShipmentDTO.setReturnShipmentCreatedAt(returnShipmentCreatedAt);
        returnShipmentDTO.setReturnShipmentDeletedAt(returnShipmentDeletedAt); //이것도 필요없대..

        boolean result = returningService.updateReturning(returnShipmentDTO);

        if(!result) {
            return "redirect:/returns/outbound/"+returnShipmentId;
        } //수정 실패 - 수정 페이지로 리다이렉트

        return "redirect:/returns/outbound/returningList"; //수정 성공 - 전체조회 페이지로 리다이렉트
    }

    //홈화면

    //비즈니스 로직 나중에 추가


}
