package com.pitchmanagement.daos;

import com.pitchmanagement.models.Booking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface BookingDao {
    void insertBooking(Booking bookingDto);
    List<Booking> getAll(@Param("status") String status);
    List<Booking> getAllByUserId(@Param("userId") Long userId, @Param("keyword") String keyword, @Param("status") String status);
    List<Booking> getAllByManagerId(@Param("managerId") Long managerId, @Param("keyword") String keyword, @Param("status") String status);
    Booking getBookingById(@Param("id") Long id);
    void updateBooking(Booking bookingDto);
    boolean isExistingBooking(@Param("subPitchId") Long subPitchId,
                              @Param("timeSlotId") Long timeSlotId,
                              @Param("bookingDate") LocalDate bookingDate,
                              @Param("status") String status);
}
