package com.ohgiraffers.warehousemanagement.wms.returning.controller;

import com.ohgiraffers.warehousemanagement.wms.returning.ReturnShipmentStatus;
import com.ohgiraffers.warehousemanagement.wms.returning.model.DTO.ReturnShipmentDTO;
import com.ohgiraffers.warehousemanagement.wms.returning.service.ReturnShipmentService;
import com.ohgiraffers.warehousemanagement.wms.store.model.dto.StoreDTO;
import com.ohgiraffers.warehousemanagement.wms.store.model.entity.Store;
import com.ohgiraffers.warehousemanagement.wms.store.repository.StoreRepository;
import com.ohgiraffers.warehousemanagement.wms.user.model.common.UserStatus;
import com.ohgiraffers.warehousemanagement.wms.user.model.dto.UserDTO;
import com.ohgiraffers.warehousemanagement.wms.user.model.entity.User;
import com.ohgiraffers.warehousemanagement.wms.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*출고반품*/
@Controller
@RequestMapping("/returns/outbound")
@Validated
public class ReturnShipmentController {

    //의존성 주입
    private final ReturnShipmentService returnShipmentService;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReturnShipmentController(
            ReturnShipmentService returnShipmentService,
            StoreRepository storeRepository,
            UserRepository userRepository) {
        this.returnShipmentService = returnShipmentService;
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/list")
    public ModelAndView getAllReturns(ModelAndView mv) {
        //서비스의 getALLReturning 호출 - 활성화된 항목만 조회
        List<ReturnShipmentDTO> returnLists = returnShipmentService.getAllReturns();

        if (returnLists != null) {
            mv.addObject("returnLists", returnLists);
            mv.setViewName("returns/outbound/list");
        } else {
            mv.addObject("returnLists", new ArrayList<>());
            mv.setViewName("returns/outbound/list");
        }

        return mv;
    }

    // 출고 ID로 로트 번호, 수량, 매장 정보 조회 API
    @GetMapping("/api/shipment-info/{shipmentId}")
    @ResponseBody
    public Map<String, Object> getShipmentInfo(@PathVariable Integer shipmentId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 출고 정보 조회
            ShipmentResponseDTO shipment = shipmentService.getShipmentById(shipmentId);
            
            if (shipment != null) {
                // 수주 ID를 통해 매장 정보 조회
                Integer salesId = shipmentService.getSaleIdByShipmentId(shipmentId);
                Integer storeId = salesService.getStoreIdBySalesId(salesId);
                
                // 매장 정보 조회
                Store store = storeRepository.findById(storeId)
                        .orElseThrow(() -> new IllegalArgumentException("매장 정보를 찾을 수 없습니다."));
                
                // 로트 번호와 수량 정보 추출
                List<String> lotNumbers = new ArrayList<>();
                List<Integer> quantities = new ArrayList<>();
                
                // shipment에서 로트번호와 수량 추출 (실제 구현에 맞게 수정 필요)
                shipment.getItems().forEach(item -> {
                    lotNumbers.add(item.getLotNumber());
                    quantities.add(item.getQuantity());
                });
                
                // 결과 저장
                result.put("status", "success");
                result.put("storeId", storeId);
                result.put("storeName", store.getStoreName());
                result.put("lotNumbers", lotNumbers);
                result.put("quantities", quantities);
            } else {
                result.put("status", "error");
                result.put("message", "출고 정보를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        
        return result;
    }

    //등록 화면 - 신규반품(뷰 화면 필요 - html필요)
    @GetMapping("/create")
    public ModelAndView registView() {
        ModelAndView mv = new ModelAndView("returns/outbound/create");

        // 활성 상태인 매장 목록 가져오기
        List<Store> activeStores = storeRepository.findAll().stream()
                .filter(store -> !store.getDeleted())
                .collect(Collectors.toList());

        // 활성 상태인 사용자 목록 가져오기
        List<User> activeUsers = userRepository.findAll().stream()
                .filter(user -> user.getUserStatus() == UserStatus.재직중)
                .collect(Collectors.toList());

        // Entity를 DTO로 변환
        List<StoreDTO> storeDTOs = activeStores.stream()
                .map(store -> new StoreDTO(
                        store.getStoreId(),
                        store.getStoreName(),
                        store.getStoreAddress(),
                        store.getStoreManagerName(),
                        store.getStoreManagerPhone(),
                        store.getStoreManagerEmail(),
                        store.getStoreCreatedAt(),
                        store.getStoreUpdatedAt(),
                        store.getStoreDeletedAt(),
                        store.getDeleted()
                ))
                .collect(Collectors.toList());

        List<UserDTO> userDTOs = activeUsers.stream()
                .map(user -> new UserDTO(
                        user.getUserId(),
                        user.getUserCode(),
                        user.getUserName(),
                        user.getUserEmail(),
                        user.getUserPhone(),
                        user.getUserPart().name(),
                        user.getUserRole().name(),
                        user.getUserStatus().name(),
                        user.getUserCreatedAt(),
                        user.getUserUpdatedAt(),
                        user.getUserDeletedAt()
                ))
                .collect(Collectors.toList());

        mv.addObject("stores", storeDTOs);
        mv.addObject("users", userDTOs);

        return mv;
    }

    @PostMapping("/create")
    public String createReturns(@Valid @ModelAttribute ReturnShipmentDTO returnShipmentDTO, RedirectAttributes rdtat) {
        System.out.println("컨트롤러에서 데이터 넘어오는지: " + returnShipmentDTO);
        int returnShipmentId = returnShipmentService.createReturns(returnShipmentDTO);
        String resultUrl = null;

        if (returnShipmentId > 0) {
            rdtat.addFlashAttribute("returnshipmentDTO", returnShipmentId);
            rdtat.addFlashAttribute("message", "반품서가 등록되었습니다.");
            resultUrl = "redirect:/returns/outbound/list";
        } else {
            rdtat.addFlashAttribute("message", "반품서 등록에 실패하였습니다.");
            resultUrl = "redirect:/returns/outbound/create";
        }
        return resultUrl;
    }

    @GetMapping("/detail/{returnShipmentId}")
    public ModelAndView getReturnsByID(@PathVariable(name = "returnShipmentId") Integer returnShipmentId,
                                       ModelAndView mv, RedirectAttributes rdtat) {
        ReturnShipmentDTO returnShipmentDTO = returnShipmentService.getReturnsByID(returnShipmentId);
        if (returnShipmentDTO != null) {
            mv.addObject("detail", returnShipmentDTO);
            mv.setViewName("returns/outbound/detail");
        } else {
            rdtat.addFlashAttribute("message", "반품 데이터를 찾을 수 없습니다.");
            mv.setViewName("redirect:/returns/outbound/list");
        }
        return mv;
    }

    @GetMapping("/delete/{returnShipmentId}")
    public String deleteReturns(@PathVariable(name = "returnShipmentId") Integer returnShipmentId,
                                RedirectAttributes redirectAttributes) {
        boolean isDeleted = returnShipmentService.deleteReturns(returnShipmentId);

        if (isDeleted) {
            redirectAttributes.addFlashAttribute("message", "반품이 성공적으로 삭제되었습니다.");
            return "redirect:/returns/outbound/list";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "삭제에 실패했습니다. 다시 시도해주세요.");
            return "redirect:/returns/outbound/detail/" + returnShipmentId;
        }
    }

    @GetMapping("/update/{returnShipmentId}")
    public ModelAndView updateReturnsById (@PathVariable("returnShipmentId") Integer
                                                   returnShipmentId, ModelAndView mv, RedirectAttributes rdtat)
    {
        ReturnShipmentDTO rsDTO = returnShipmentService.getReturnsByID(returnShipmentId);

        if (rsDTO != null) {
            // 활성 상태인 매장 목록 가져오기
            List<Store> activeStores = storeRepository.findAll().stream()
                    .filter(store -> !store.getDeleted())
                    .collect(Collectors.toList());

            // 활성 상태인 사용자 목록 가져오기
            List<User> activeUsers = userRepository.findAll().stream()
                    .filter(user -> user.getUserStatus() == UserStatus.재직중)
                    .collect(Collectors.toList());

            // Entity를 DTO로 변환
            List<StoreDTO> storeDTOs = activeStores.stream()
                    .map(store -> new StoreDTO(
                            store.getStoreId(),
                            store.getStoreName(),
                            store.getStoreAddress(),
                            store.getStoreManagerName(),
                            store.getStoreManagerPhone(),
                            store.getStoreManagerEmail(),
                            store.getStoreCreatedAt(),
                            store.getStoreUpdatedAt(),
                            store.getStoreDeletedAt(),
                            store.getDeleted()
                    ))
                    .collect(Collectors.toList());

            List<UserDTO> userDTOs = activeUsers.stream()
                    .map(user -> new UserDTO(
                            user.getUserId(),
                            user.getUserCode(),
                            user.getUserName(),
                            user.getUserEmail(),
                            user.getUserPhone(),
                            user.getUserPart().name(),
                            user.getUserRole().name(),
                            user.getUserStatus().name(),
                            user.getUserCreatedAt(),
                            user.getUserUpdatedAt(),
                            user.getUserDeletedAt()
                    ))
                    .collect(Collectors.toList());

            mv.addObject("stores", storeDTOs);
            mv.addObject("users", userDTOs);
            mv.addObject("ReturnShipmentDTO", rsDTO);
            mv.setViewName("returns/outbound/update");
        } else {
            rdtat.addFlashAttribute("message", "반품 데이터를 찾을 수 없습니다.");
            mv.setViewName("redirect:/returns/outbound/list");
        }

        return mv;
    }

    @PostMapping("/update/{returnShipmentId}")
    public String updateReturns(@PathVariable("returnShipmentId") Integer returnShipmentId,
                                @Valid @ModelAttribute ReturnShipmentDTO returnShipmentDTO,
                                RedirectAttributes rdtat) {
        ReturnShipmentDTO updateDTO = returnShipmentService.updateReturns(returnShipmentId, returnShipmentDTO);
        String resultUrl = null;

        if (updateDTO != null) {
            rdtat.addFlashAttribute("returnShipmentDTO", updateDTO);
            rdtat.addFlashAttribute("message", "반품서를 수정했습니다.");
            resultUrl = "redirect:/returns/outbound/list";
        } else {
            rdtat.addFlashAttribute("message", "반품서 수정에 실패했습니다. 다시 시도해주세요");
            resultUrl = "redirect:/returns/outbound/detail/" + returnShipmentId;
        }
        return resultUrl;
    }

    @PostMapping("/update/{returnShipmentId}/status")
    public String updateStatusReturns(@PathVariable("returnShipmentId") Integer returnShipmentId,
                                      @RequestParam ReturnShipmentStatus returnShipmentStatus,
                                      RedirectAttributes rdtat) {
        boolean result = returnShipmentService.updateStatusReturns(returnShipmentId, returnShipmentStatus);
        String resultUrl = null;

        if (result) {
            rdtat.addFlashAttribute("message", "반품 상태가 변경되었습니다.");
            resultUrl = "redirect:/returns/outbound/list";
        } else {
            rdtat.addFlashAttribute("message", "상태변경에 실패했습니다. 다시 시도해주세요");
            resultUrl = "redirect:/returns/outbound/detail/" + returnShipmentId;
        }
        return resultUrl;
    }
}