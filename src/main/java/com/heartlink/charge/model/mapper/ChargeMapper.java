package com.heartlink.charge.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.heartlink.charge.model.dto.ChargeDto;

@Mapper
public interface ChargeMapper {

    public int getCurrentSequence();

    public int setPaymentHistory(ChargeDto chargeDto);

    public ChargeDto setPaymentDbDetails(int paymentNo);
}
