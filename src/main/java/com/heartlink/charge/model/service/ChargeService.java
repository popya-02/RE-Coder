package com.heartlink.charge.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heartlink.charge.model.dto.ChargeDto;
import com.heartlink.charge.model.mapper.ChargeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ChargeService {

    private final ChargeMapper chargeMapper;
    private final RestTemplate restTemplate;

    @Value("${portone.api.secret}")
    private String portoneApiSecret;

    @Autowired
    public ChargeService(ChargeMapper chargeMapper,
                         RestTemplate restTemplate){
        this.chargeMapper = chargeMapper;
        this.restTemplate = restTemplate;
    }

    public int getCurrentSequence(){
        return chargeMapper.getCurrentSequence();
    }

    public int setPaymentHistory(ChargeDto chargeDto){
        return chargeMapper.setPaymentHistory(chargeDto);
    }

    public ChargeDto getPaymentDetails(int paymentNo) {
        String url = "https://api.portone.io/payments/" + paymentNo;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "PortOne " + portoneApiSecret);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 포트원 API 호출
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        System.out.println(responseEntity.toString());

        // JSON 응답을 Map으로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> responseMap;
        try {
            responseMap = objectMapper.readValue(responseEntity.getBody(), Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON response", e);
        }

        ChargeDto responseChargeDto = new ChargeDto();
        responseChargeDto.setPaymentNo(paymentNo);
//        responseChargeDto.setPaymentAmount((Integer) responseMap.get("payment_amount"));
//        responseChargeDto.setPaymentMethod(paymentResponse.getPaymentMethod());
//        responseChargeDto.setPaymentProduct(paymentResponse.getPaymentProduct());

        return responseChargeDto;
    }

    public ChargeDto setPaymentDbDetails(int paymenyNo){
        return chargeMapper.setPaymentDbDetails(paymenyNo);
    }

}
