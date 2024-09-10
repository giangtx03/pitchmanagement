package com.pitchmanagement.services;

import com.pitchmanagement.models.requests.booking.CreateBookingRequest;
import com.pitchmanagement.models.requests.booking.UpdateBookingRequest;
import com.pitchmanagement.models.responses.BookingResponse;
import com.pitchmanagement.models.responses.PageResponse;

public interface BookingService {
    BookingResponse createBooking(CreateBookingRequest createBookingRequest) throws Exception;
    PageResponse getAllByUserId(Long userId);
    PageResponse getAllByManagerId(Long managerId);
    BookingResponse getBookingById(Long id);
    BookingResponse updateBooking(UpdateBookingRequest updateBookingRequest) throws Exception;
}
