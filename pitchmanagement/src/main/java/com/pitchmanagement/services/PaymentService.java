package com.pitchmanagement.services;

public interface PaymentService {

    String createPayment(int amount, String paymentType, Long userId, Long bookingId)  throws Exception;

}
