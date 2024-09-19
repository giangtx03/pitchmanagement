package com.pitchmanagement.daos;

import com.pitchmanagement.dtos.PaymentDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PaymentDao {
    void insertPayment(PaymentDto paymentDto);
    PaymentDto getPaymentById(@Param("id") Long id);
    List<PaymentDto> getPaymentByUserId(@Param("userId") Long userId, @Param("paymentType") String paymentType);
    List<PaymentDto> getPaymentByManagerId(@Param("managerId") Long managerId, @Param("paymentType") String paymentType);

}
