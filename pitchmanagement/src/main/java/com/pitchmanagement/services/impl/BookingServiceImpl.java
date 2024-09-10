package com.pitchmanagement.services.impl;

import com.pitchmanagement.daos.*;
import com.pitchmanagement.models.requests.booking.CreateBookingRequest;
import com.pitchmanagement.models.requests.booking.UpdateBookingRequest;
import com.pitchmanagement.models.responses.BookingResponse;
import com.pitchmanagement.models.responses.PageResponse;
import com.pitchmanagement.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingDao bookingDao;
    private final UserDao userDao;
    private final TimeSlotDao timeSlotDao;
    private final PitchTimeDao pitchTimeDao;
    private final SubPitchDao subPitchDao;

    @Override
    public BookingResponse createBooking(CreateBookingRequest createBookingRequest) throws Exception {
        return null;
    }

    @Override
    public PageResponse getAllByUserId(Long userId) {
        return null;
    }

    @Override
    public PageResponse getAllByManagerId(Long managerId) {
        return null;
    }

    @Override
    public BookingResponse getBookingById(Long id) {
        return null;
    }

    @Override
    public BookingResponse updateBooking(UpdateBookingRequest updateBookingRequest) throws Exception {
        return null;
    }
}
