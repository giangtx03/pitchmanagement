package com.pitchmanagement.services;

import com.pitchmanagement.models.requests.payment.VNPayRequest;

public interface PaymentService {

    String createPayment(VNPayRequest vnPayRequest)  throws Exception;
    void vnpayReturn(Long bookingId, int amount,String note, String paymentType, String bankCode, String orderInfo, String responseCode, String transactionStatus) throws Exception;
}
