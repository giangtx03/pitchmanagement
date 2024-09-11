package com.pitchmanagement.constants;

public enum BookingStatus {
    PENDING,           // Chờ xử lý
    CONFIRMED,         // Đã xác nhận
    CANCELLED,         // Đã hủy
    DEPOSIT_PAID,      // Đã thanh toán cọc
    PAID,              // Đã thanh toán toàn bộ
    UNPAID,            // Chưa thanh toán
    IN_PROGRESS,       // Đang diễn ra
    COMPLETED,         // Hoàn tất
    NO_SHOW            // Không đến
}
