package com.pitchmanagement.daos;

import com.pitchmanagement.models.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PaymentDao {
    void insertPayment(Payment payment);
    Payment getPaymentById(@Param("id") Long id);
    List<Payment> getPaymentByManagerId(@Param("managerId") Long managerId, @Param("keyword") String keyword , @Param("paymentType") String paymentType);

}
