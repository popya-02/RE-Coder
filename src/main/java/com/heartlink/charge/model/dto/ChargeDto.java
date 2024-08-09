package com.heartlink.charge.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자 생성
@AllArgsConstructor // 모든 변수가 있는 생성자
@ToString
public class ChargeDto {

    private int paymentNo;

    private String paymentUserNo;
    private String paymentDate;

    private int paymentAmount;

    private String paymentMethod;
    private String paymentState;
    private String paymentProduct;
    private String paymentReference;



}
