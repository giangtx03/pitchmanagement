package com.pitchmanagement.daos;

import com.pitchmanagement.dtos.BookingDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookingDao {
    void insertBooking(BookingDto bookingDto);
    List<BookingDto> getAllByUserId(@Param("userId") Long userId);
    List<BookingDto> getAllByManagerId(@Param("managerId") Long managerId);
    BookingDto getBookingById(@Param("id") Long id);
    void updateBooking(BookingDto bookingDto);
}
