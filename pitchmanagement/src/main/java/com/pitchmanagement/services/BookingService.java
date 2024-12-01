package com.pitchmanagement.services;

import com.pitchmanagement.models.requests.booking.CancelRequest;
import com.pitchmanagement.models.requests.booking.CreateBookingRequest;
import com.pitchmanagement.models.requests.booking.UpdateBookingRequest;
import com.pitchmanagement.models.responses.BookingResponse;
import com.pitchmanagement.models.responses.PageResponse;
import org.apache.ibatis.javassist.NotFoundException;

public interface BookingService {
    BookingResponse createBooking(CreateBookingRequest createBookingRequest) throws NotFoundException;
    PageResponse getAllByUserId(Long userId,String keyword, int pageNumber, int limit, String status);
    PageResponse getAllByManagerId(Long managerId, String keyword, int pageNumber, int limit, String status);
    BookingResponse getBookingById(Long id) throws Exception;
    void requestCancelBooking(Long bookingId, CancelRequest cancelRequest) throws Exception;
}
