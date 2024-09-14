package com.pitchmanagement.services;

public interface PaymentService {

    String createPayment(String paymentType, Long userId, Long bookingId)  throws Exception;
    void vnpayReturn(Long bookingId, int amount, String bankCode, String orderInfo, String responseCode, String transactionStatus) throws Exception;
}
