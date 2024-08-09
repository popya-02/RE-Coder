package com.heartlink.charge.contoller;

import com.heartlink.charge.model.dto.ChargeDto;
import com.heartlink.charge.model.service.ChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/charge")
public class ChargeController {

    private final ChargeService chargeService;

    @Autowired
    public ChargeController(ChargeService chargeService){
        this.chargeService = chargeService;
    }

    @GetMapping("/shop")
    public String moveMain(){
        return "mypage/mypage_coin_charge/charge_main";
    }

    @GetMapping("/history")
    public String cashpage() {
        return "mypage/mypage_coin_charge/charge_history";
    }

    @GetMapping("/sequence")
    @ResponseBody
    public int getSequence(){
        int currentSequence = chargeService.getCurrentSequence();

        return currentSequence;
    }

    @PostMapping("/pending")
    @ResponseBody
    public String paymentState(@RequestBody ChargeDto chargeDto){
        chargeDto.setPaymentState("Pending");
        chargeDto.setPaymentMethod("CARD");

        int setPaymentHistory = chargeService.setPaymentHistory(chargeDto);

        if(setPaymentHistory != 1){
            return "failed";
        }

        return "success";
    }

    @PostMapping("/complete")
    @ResponseBody
    public ResponseEntity<?> completePayment(@RequestBody ChargeDto chargeDto) {
        System.out.println("Received paymentNo: " + chargeDto.getPaymentNo());

        int paymentNo = chargeDto.getPaymentNo();

        try {
            // 유효성 검사 후 결제 상세 정보 조회
            ChargeDto response = chargeService.getPaymentDetails(paymentNo);

            System.out.println("response" + response.toString());

            if (response == null) {
                // 결제 정보가 없는 경우
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment details not found for paymentNo: " + paymentNo);
            }

            ChargeDto dbResponse = chargeService.setPaymentDbDetails(paymentNo);
            System.out.println("dbResponse" + dbResponse.toString());

            System.out.println("Payment Method: " + response.getPaymentMethod());
            System.out.println("Payment Method: " + dbResponse.getPaymentMethod());

            // 정상 응답 반환
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 예외 발생 시 오류 응답 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }


}
