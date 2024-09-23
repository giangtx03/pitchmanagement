package com.pitchmanagement.services;

import com.pitchmanagement.models.requests.payment.VNPayRequest;
import com.pitchmanagement.models.responses.PageResponse;

public interface PaymentService {

    String createPayment(VNPayRequest vnPayRequest)  throws Exception;
    void vnpayReturn(Long bookingId, int amount,String note, String paymentType, String bankCode, String orderInfo, String responseCode, String transactionStatus) throws Exception;
    PageResponse getAllPaymentByManagerId(Long managerId, String keyword, String paymentType, int pageNumber, int limit) throws Exception;
}
