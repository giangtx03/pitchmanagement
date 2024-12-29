package com.pitchmanagement.services;

import com.pitchmanagement.dtos.requests.booking.CancelRequest;
import com.pitchmanagement.dtos.requests.booking.CreateBookingRequest;
import com.pitchmanagement.dtos.responses.BookingResponse;
import com.pitchmanagement.dtos.responses.PageResponse;
import org.apache.ibatis.javassist.NotFoundException;

public interface BookingService {
    BookingResponse createBooking(CreateBookingRequest createBookingRequest) throws NotFoundException;
    PageResponse getAllByUserId(Long userId,String keyword, int pageNumber, int limit, String status);
    PageResponse getAllByManagerId(Long managerId, String keyword, int pageNumber, int limit, String status);
    BookingResponse getBookingById(Long id) throws Exception;
    void requestCancelBooking(Long bookingId, CancelRequest cancelRequest) throws Exception;
}
