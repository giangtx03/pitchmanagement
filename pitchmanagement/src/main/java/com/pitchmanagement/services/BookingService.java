package com.pitchmanagement.services;

import com.pitchmanagement.models.requests.booking.CreateBookingRequest;
import com.pitchmanagement.models.requests.booking.UpdateBookingRequest;
import com.pitchmanagement.models.responses.BookingResponse;
import com.pitchmanagement.models.responses.PageResponse;

public interface BookingService {
    BookingResponse createBooking(CreateBookingRequest createBookingRequest) throws Exception;
    PageResponse getAllByUserId(Long userId,String keyword, int pageNumber, int limit, String status);
    PageResponse getAllByManagerId(Long managerId, String keyword, int pageNumber, int limit, String status);
    BookingResponse getBookingById(Long id) throws Exception;
}
