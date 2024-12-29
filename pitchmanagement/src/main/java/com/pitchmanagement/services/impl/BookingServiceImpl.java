package com.pitchmanagement.services.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pitchmanagement.constants.AppConstant;
import com.pitchmanagement.enums.BookingStatus;
import com.pitchmanagement.daos.*;
import com.pitchmanagement.models.*;
import com.pitchmanagement.dtos.requests.booking.CancelRequest;
import com.pitchmanagement.dtos.requests.booking.CreateBookingRequest;
import com.pitchmanagement.dtos.responses.BookingResponse;
import com.pitchmanagement.dtos.responses.PageResponse;
import com.pitchmanagement.dtos.responses.UserResponse;
import com.pitchmanagement.dtos.responses.pitch.PitchResponse;
import com.pitchmanagement.dtos.responses.pitch.PitchTimeResponse;
import com.pitchmanagement.dtos.responses.pitch.SubPitchResponse;
import com.pitchmanagement.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingDao bookingDao;
    private final UserDao userDao;
    private final PitchTimeDao pitchTimeDao;
    private final SubPitchDao subPitchDao;
    private final PitchDao pitchDao;
    private final ImageDao imageDao;
    private final TimeSlotDao timeSlotDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BookingResponse createBooking(CreateBookingRequest request) throws NotFoundException {
        if(bookingDao.isExistingBooking(request.getSubPitchId(),
                request.getTimeSlotId(),
                request.getBookingDate(),
                BookingStatus.CANCELLED.toString())){
            throw new RuntimeException("Sân trong khung giờ này đã hết!");
        }

        User userDto = Optional.ofNullable(userDao.getUserById(request.getUserId()))
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Người đặt sân không hợp lệ!"));
        PitchTime pitchTime = Optional.ofNullable(pitchTimeDao.getBySubPitchIdAndTimeSlotId(request.getSubPitchId(),request.getTimeSlotId()))
                .orElseThrow(() -> new NotFoundException("Sân và khung giờ không hợp lệ!"));
        SubPitch subPitchDto = Optional.ofNullable(subPitchDao.getSubPitchById(request.getSubPitchId()))
                .orElseThrow(() -> new NotFoundException("Sân con không hợp lệ!"));
        Pitch pitch = Optional.ofNullable(pitchDao.getPitchById(subPitchDto.getPitchId(), true))
                .orElseThrow(() -> new NotFoundException("Sân không hợp lệ!"));
        List<Image> imageDtoList = imageDao.getAllByPitchId(pitch.getId());
        User managerDto = Optional.ofNullable(userDao.getUserById(pitch.getManagerId()))
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Người quản lý không hợp lệ!"));
        if(!pitchTime.isActive() || !pitch.isActive() || !subPitchDto.isActive()){
            throw new RuntimeException("Sân và khung giờ không hoạt động!");
        }

        if(request.getBookingDate().isEqual(LocalDate.now())){
            if(pitchTime.getStartTime().isBefore(LocalTime.now())){
                throw new RuntimeException("Sân và khung giờ không hoạt động!");
            }
        }

        Booking bookingDto = Booking.builder()
                .status(BookingStatus.PENDING.toString())
                .subPitchId(request.getSubPitchId())
                .timeSlotId(request.getTimeSlotId())
                .userId(request.getUserId())
                .bookingDate(request.getBookingDate())
                .note(request.getNote())
                .deposit((float) (pitchTime.getPrice() * 0.5))
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
        bookingDao.insertBooking(bookingDto);

        PitchResponse pitchResponse = PitchResponse.builder()
                .id(pitch.getId())
                .name(pitch.getName())
                .location(pitch.getLocation())
                .images(imageDtoList.stream().map(Image::getName).toList())
                .manager(UserResponse.fromUserDto(managerDto))
                .isActive(pitch.isActive())
                .build();

        SubPitchResponse subPitchResponse = SubPitchResponse.builder()
                .name(subPitchDto.getName())
                .pitchType(subPitchDto.getPitchType())
                .isActive(subPitchDto.isActive())
                .build();

        PitchTimeResponse pitchTimeResponse = PitchTimeResponse.builder()
                .startTime(pitchTime.getStartTime())
                .endTime(pitchTime.getEndTime())
                .price(pitchTime.getPrice())
                .isActive(pitchTime.isActive())
                .build();

        BookingResponse bookingResponse = BookingResponse.toBookingResponse(
                bookingDto, UserResponse.fromUserDto(userDto), pitchResponse, subPitchResponse, pitchTimeResponse
        );
        return bookingResponse;
    }

    @Override
    public PageResponse getAllByUserId(Long userId,String keyword, int pageNumber, int limit, String status) {
        PageHelper.startPage(pageNumber, limit);
        PageHelper.orderBy("b.update_at DESC");
        List<Booking> bookingDtoList = bookingDao.getAllByUserId(userId,keyword, status);
        PageInfo<Booking> pageInfo = new PageInfo<>(bookingDtoList);

        List<BookingResponse> bookingResponseList = bookingDtoList.stream().map(
                bookingDto -> {
                    PitchTime pitchTime = pitchTimeDao.getBySubPitchIdAndTimeSlotId(bookingDto.getSubPitchId(),bookingDto.getTimeSlotId());
                    SubPitch subPitchDto = subPitchDao.getSubPitchById(bookingDto.getSubPitchId());
                    Pitch pitch = pitchDao.getPitchById(subPitchDto.getPitchId(), false);
                    List<Image> imageDtoList = imageDao.getAllByPitchId(pitch.getId());
                    User managerDto = userDao.getUserById(pitch.getManagerId());

                    PitchResponse pitchResponse = PitchResponse.builder()
                            .id(pitch.getId())
                            .name(pitch.getName())
                            .location(pitch.getLocation())
                            .images(imageDtoList.stream().map(Image::getName).toList())
                            .manager(UserResponse.fromUserDto(managerDto))
                            .isActive(pitch.isActive())
                            .avgStar(pitch.getAvgStar())
                            .build();

                    SubPitchResponse subPitchResponse = SubPitchResponse.builder()
                            .name(subPitchDto.getName())
                            .pitchType(subPitchDto.getPitchType())
                            .isActive(subPitchDto.isActive())
                            .build();

                    PitchTimeResponse pitchTimeResponse = PitchTimeResponse.builder()
                            .startTime(pitchTime.getStartTime())
                            .endTime(pitchTime.getEndTime())
                            .price(pitchTime.getPrice())
                            .isActive(pitchTime.isActive())
                            .build();

                    BookingResponse bookingResponse = BookingResponse.toBookingResponse(
                            bookingDto, null, pitchResponse, subPitchResponse, pitchTimeResponse
                    );
                    return bookingResponse;
                }
        ).toList();

        PageResponse pageResponse = PageResponse.builder()
                .totalItems(pageInfo.getTotal())
                .totalPages(pageInfo.getPages())
                .items(bookingResponseList)
                .build();
        return pageResponse;
    }

    @Override
    public PageResponse getAllByManagerId(Long managerId,String keyword, int pageNumber, int limit, String status) {
        PageHelper.startPage(pageNumber, limit);
        if(!status.trim().isEmpty() && status.equals(BookingStatus.PENDING.toString())){
            PageHelper.orderBy("b.create_at ASC");
        }
        else{
            PageHelper.orderBy("b.update_at DESC");
        }
        List<Booking> bookingDtoList = bookingDao.getAllByManagerId(managerId,keyword, status);
        PageInfo<Booking> pageInfo = new PageInfo<>(bookingDtoList);

        List<BookingResponse> bookingResponseList = bookingDtoList.stream().map(
                bookingDto -> {
                    PitchTime pitchTime = pitchTimeDao.getBySubPitchIdAndTimeSlotId(bookingDto.getSubPitchId(),bookingDto.getTimeSlotId());
                    SubPitch subPitchDto = subPitchDao.getSubPitchById(bookingDto.getSubPitchId());
                    Pitch pitch = pitchDao.getPitchById(subPitchDto.getPitchId(), false);
                    List<Image> imageDtoList = imageDao.getAllByPitchId(pitch.getId());
                    User userDto = userDao.getUserById(bookingDto.getUserId());

                    PitchResponse pitchResponse = PitchResponse.builder()
                            .id(pitch.getId())
                            .name(pitch.getName())
                            .location(pitch.getLocation())
                            .images(imageDtoList.stream().map(Image::getName).toList())
                            .isActive(pitch.isActive())
                            .build();

                    SubPitchResponse subPitchResponse = SubPitchResponse.builder()
                            .name(subPitchDto.getName())
                            .pitchType(subPitchDto.getPitchType())
                            .isActive(subPitchDto.isActive())
                            .build();

                    PitchTimeResponse pitchTimeResponse = PitchTimeResponse.builder()
                            .startTime(pitchTime.getStartTime())
                            .endTime(pitchTime.getEndTime())
                            .price(pitchTime.getPrice())
                            .isActive(pitchTime.isActive())
                            .build();

                    BookingResponse bookingResponse = BookingResponse.toBookingResponse(
                            bookingDto, UserResponse.fromUserDto(userDto), pitchResponse, subPitchResponse, pitchTimeResponse
                    );
                    return bookingResponse;
                }
        ).toList();

        PageResponse pageResponse = PageResponse.builder()
                .totalItems(pageInfo.getTotal())
                .totalPages(pageInfo.getPages())
                .items(bookingResponseList)
                .build();
        return pageResponse;
    }

    @Override
    public BookingResponse getBookingById(Long id) throws Exception {
        Booking bookingDto = Optional.ofNullable(bookingDao.getBookingById(id))
                .orElseThrow(() -> new NotFoundException("Đơn đặt không hợp lệ!"));

        PitchTime pitchTime = pitchTimeDao.getBySubPitchIdAndTimeSlotId(bookingDto.getSubPitchId(),bookingDto.getTimeSlotId());
        SubPitch subPitchDto = subPitchDao.getSubPitchById(bookingDto.getSubPitchId());
        Pitch pitch = pitchDao.getPitchById(subPitchDto.getPitchId(), true);
        List<Image> imageDtoList = imageDao.getAllByPitchId(pitch.getId());
        User userDto = userDao.getUserById(bookingDto.getUserId());
        User managerDto = userDao.getUserById(pitch.getManagerId());

        PitchResponse pitchResponse = PitchResponse.builder()
                .id(pitch.getId())
                .name(pitch.getName())
                .location(pitch.getLocation())
                .images(imageDtoList.stream().map(Image::getName).toList())
                .manager(UserResponse.fromUserDto(managerDto))
                .build();

        SubPitchResponse subPitchResponse = SubPitchResponse.builder()
                .name(subPitchDto.getName())
                .pitchType(subPitchDto.getPitchType())
                .build();

        PitchTimeResponse pitchTimeResponse = PitchTimeResponse.builder()
                .startTime(pitchTime.getStartTime())
                .endTime(pitchTime.getEndTime())
                .price(pitchTime.getPrice())
                .build();

        BookingResponse bookingResponse = BookingResponse.toBookingResponse(
                bookingDto, UserResponse.fromUserDto(userDto), pitchResponse, subPitchResponse, pitchTimeResponse
        );
        return bookingResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void requestCancelBooking(Long bookingId, CancelRequest cancelRequest) throws Exception {
        Booking bookingDto = Optional.ofNullable(bookingDao.getBookingById(bookingId))
                .orElseThrow(() -> new NotFoundException("Đơn đặt không hợp lệ!"));

        if(cancelRequest.getCaseCancel().equals(AppConstant.CANCEL_NONE) ||
                (cancelRequest.getCaseCancel().equals(AppConstant.CANCEL_WEATHER)
                && bookingDto.getStatus().equals(BookingStatus.PENDING.toString())
        )){
            bookingDto.setStatus(BookingStatus.CANCELLED.toString());
            bookingDto.setUpdateAt(LocalDateTime.now());
            bookingDao.updateBooking(bookingDto);
        }else if(cancelRequest.getCaseCancel().equals(AppConstant.CANCEL_WEATHER) ||
                (cancelRequest.getCaseCancel().equals(AppConstant.CANCEL_REFUND) &&
                bookingDto.getBookingDate().compareTo(LocalDate.now().plusDays(1L)) == 1)){
            bookingDto.setStatus(BookingStatus.REQUEST_CANCELLATION.toString());
            bookingDto.setUpdateAt(LocalDateTime.now());
            bookingDao.updateBooking(bookingDto);
        }
        else{
            throw new RuntimeException("Trường hợp hủy không xác định!");
        }
    }

    @Scheduled(fixedDelay = 60000)
    public void checkPendingPayments(){
        List<Booking> pendingBookings = bookingDao.getAll(BookingStatus.PENDING.toString());
        List<Booking> depositBookings = bookingDao.getAll(BookingStatus.DEPOSIT_PAID.toString());

        for(Booking booking : pendingBookings){
            if (booking.getCreateAt().plusMinutes(30).isBefore(LocalDateTime.now())) {
                // Hủy đơn đặt hàng
                booking.setStatus(BookingStatus.CANCELLED.toString());
                booking.setUpdateAt(LocalDateTime.now());
                bookingDao.updateBooking(booking);
            }
        }

        for(Booking booking : depositBookings){
            if(booking.getBookingDate().isBefore(LocalDate.now())){
                booking.setStatus(BookingStatus.NO_SHOW.toString());
                bookingDao.updateBooking(booking);
            }
            else if(booking.getBookingDate().isEqual(LocalDate.now())){
                TimeSlot timeSlotDto = timeSlotDao.getTimeSlotById(booking.getTimeSlotId());
                if(timeSlotDto.getEndTime().plusMinutes(30).isBefore(LocalTime.now())){
                    booking.setStatus(BookingStatus.NO_SHOW.toString());
                    bookingDao.updateBooking(booking);
                }
            }
        }
    }
}
