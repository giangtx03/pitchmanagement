package com.pitchmanagement.enums;

public enum BookingStatus {
    PENDING,           // Chờ xử lý
    CANCELLED,         // Đã hủy
    REQUEST_CANCELLATION, // yêu cầu hủy
    DEPOSIT_PAID,      // Đã thanh toán cọc
    PAID,              // Đã thanh toán toàn bộ
    COMPLETED,         // Hoàn tất
    NO_SHOW            // Không đến
}
