package com.pitchmanagement.daos;

import com.pitchmanagement.dtos.BookingDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface BookingDao {
    void insertBooking(BookingDto bookingDto);
    List<BookingDto> getAllByUserId(@Param("userId") Long userId, @Param("status") String status);
    List<BookingDto> getAllByManagerId(@Param("managerId") Long managerId, @Param("status") String status);
    BookingDto getBookingById(@Param("id") Long id);
    void updateBooking(BookingDto bookingDto);
    boolean isExistingBooking(@Param("subPitchId") Long subPitchId,
                              @Param("timeSlotId") Long timeSlotId,
                              @Param("bookingDate") LocalDate bookingDate,
                              @Param("status") String status);
}