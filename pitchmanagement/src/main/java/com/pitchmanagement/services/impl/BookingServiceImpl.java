package com.pitchmanagement.services.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pitchmanagement.constants.BookingStatus;
import com.pitchmanagement.daos.*;
import com.pitchmanagement.dtos.*;
import com.pitchmanagement.models.requests.booking.CreateBookingRequest;
import com.pitchmanagement.models.requests.booking.UpdateBookingRequest;
import com.pitchmanagement.models.responses.BookingResponse;
import com.pitchmanagement.models.responses.PageResponse;
import com.pitchmanagement.models.responses.UserResponse;
import com.pitchmanagement.models.responses.pitch.PitchResponse;
import com.pitchmanagement.models.responses.pitch.PitchTimeResponse;
import com.pitchmanagement.models.responses.pitch.SubPitchResponse;
import com.pitchmanagement.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BookingResponse createBooking(CreateBookingRequest request) throws Exception {
        if(bookingDao.isExistingBooking(request.getSubPitchId(),
                request.getTimeSlotId(),
                request.getBookingDate(),
                BookingStatus.CANCELLED.toString())){
            throw new RuntimeException("Sân trong khung giờ này đã hết!");
        }

        UserDto userDto = Optional.ofNullable(userDao.getUserById(request.getUserId()))
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Người đặt sân không hợp lệ!"));
        PitchTimeDto pitchTimeDto = Optional.ofNullable(pitchTimeDao.getBySubPitchIdAndTimeSlotId(request.getSubPitchId(),request.getTimeSlotId()))
                .orElseThrow(() -> new NotFoundException("Sân và khung giờ không hợp lệ!"));
        SubPitchDto subPitchDto = Optional.ofNullable(subPitchDao.getSubPitchById(request.getSubPitchId()))
                .orElseThrow(() -> new NotFoundException("Sân con không hợp lệ!"));
        PitchDto pitchDto = Optional.ofNullable(pitchDao.getPitchById(subPitchDto.getPitchId(), true))
                .orElseThrow(() -> new NotFoundException("Sân không hợp lệ!"));
        List<ImageDto> imageDtoList = imageDao.getAllByPitchId(pitchDto.getId());
        UserDto managerDto = Optional.ofNullable(userDao.getUserById(pitchDto.getManagerId()))
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Người quản lý không hợp lệ!"));
        if(!pitchTimeDto.isActive() || !pitchDto.isActive() || !subPitchDto.isActive()){
            throw new RuntimeException("Sân và khung giờ không hoạt động!");
        }

        BookingDto bookingDto = BookingDto.builder()
                .status(BookingStatus.PENDING.toString())
                .subPitchId(request.getSubPitchId())
                .timeSlotId(request.getTimeSlotId())
                .userId(request.getUserId())
                .bookingDate(request.getBookingDate())
                .note(request.getNote())
                .deposit((float) (pitchTimeDto.getPrice() * 0.5))
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
        bookingDao.insertBooking(bookingDto);

        PitchResponse pitchResponse = PitchResponse.builder()
                .id(pitchDto.getId())
                .name(pitchDto.getName())
                .location(pitchDto.getLocation())
                .images(imageDtoList.stream().map(ImageDto::getName).toList())
                .manager(UserResponse.fromUserDto(managerDto))
                .isActive(pitchDto.isActive())
                .build();

        SubPitchResponse subPitchResponse = SubPitchResponse.builder()
                .name(subPitchDto.getName())
                .pitchType(subPitchDto.getPitchType())
                .isActive(subPitchDto.isActive())
                .build();

        PitchTimeResponse pitchTimeResponse = PitchTimeResponse.builder()
                .startTime(pitchTimeDto.getStartTime())
                .endTime(pitchTimeDto.getEndTime())
                .price(pitchTimeDto.getPrice())
                .isActive(pitchTimeDto.isActive())
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
        List<BookingDto> bookingDtoList = bookingDao.getAllByUserId(userId,keyword, status);
        PageInfo<BookingDto> pageInfo = new PageInfo<>(bookingDtoList);

        List<BookingResponse> bookingResponseList = bookingDtoList.stream().map(
                bookingDto -> {
                    PitchTimeDto pitchTimeDto = pitchTimeDao.getBySubPitchIdAndTimeSlotId(bookingDto.getSubPitchId(),bookingDto.getTimeSlotId());
                    SubPitchDto subPitchDto = subPitchDao.getSubPitchById(bookingDto.getSubPitchId());
                    PitchDto pitchDto = pitchDao.getPitchById(subPitchDto.getPitchId(), false);
                    List<ImageDto> imageDtoList = imageDao.getAllByPitchId(pitchDto.getId());
                    UserDto managerDto = userDao.getUserById(pitchDto.getManagerId());

                    PitchResponse pitchResponse = PitchResponse.builder()
                            .id(pitchDto.getId())
                            .name(pitchDto.getName())
                            .location(pitchDto.getLocation())
                            .images(imageDtoList.stream().map(ImageDto::getName).toList())
                            .manager(UserResponse.fromUserDto(managerDto))
                            .isActive(pitchDto.isActive())
                            .avgStar(pitchDto.getAvgStar())
                            .build();

                    SubPitchResponse subPitchResponse = SubPitchResponse.builder()
                            .name(subPitchDto.getName())
                            .pitchType(subPitchDto.getPitchType())
                            .isActive(subPitchDto.isActive())
                            .build();

                    PitchTimeResponse pitchTimeResponse = PitchTimeResponse.builder()
                            .startTime(pitchTimeDto.getStartTime())
                            .endTime(pitchTimeDto.getEndTime())
                            .price(pitchTimeDto.getPrice())
                            .isActive(pitchTimeDto.isActive())
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
        List<BookingDto> bookingDtoList = bookingDao.getAllByManagerId(managerId,keyword, status);
        PageInfo<BookingDto> pageInfo = new PageInfo<>(bookingDtoList);

        List<BookingResponse> bookingResponseList = bookingDtoList.stream().map(
                bookingDto -> {
                    PitchTimeDto pitchTimeDto = pitchTimeDao.getBySubPitchIdAndTimeSlotId(bookingDto.getSubPitchId(),bookingDto.getTimeSlotId());
                    SubPitchDto subPitchDto = subPitchDao.getSubPitchById(bookingDto.getSubPitchId());
                    PitchDto pitchDto = pitchDao.getPitchById(subPitchDto.getPitchId(), false);
                    List<ImageDto> imageDtoList = imageDao.getAllByPitchId(pitchDto.getId());
                    UserDto userDto = userDao.getUserById(bookingDto.getUserId());

                    PitchResponse pitchResponse = PitchResponse.builder()
                            .id(pitchDto.getId())
                            .name(pitchDto.getName())
                            .location(pitchDto.getLocation())
                            .images(imageDtoList.stream().map(ImageDto::getName).toList())
                            .isActive(pitchDto.isActive())
                            .build();

                    SubPitchResponse subPitchResponse = SubPitchResponse.builder()
                            .name(subPitchDto.getName())
                            .pitchType(subPitchDto.getPitchType())
                            .isActive(subPitchDto.isActive())
                            .build();

                    PitchTimeResponse pitchTimeResponse = PitchTimeResponse.builder()
                            .startTime(pitchTimeDto.getStartTime())
                            .endTime(pitchTimeDto.getEndTime())
                            .price(pitchTimeDto.getPrice())
                            .isActive(pitchTimeDto.isActive())
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
        BookingDto bookingDto = Optional.ofNullable(bookingDao.getBookingById(id))
                .orElseThrow(() -> new NotFoundException("Đơn đặt không hợp lệ!"));

        PitchTimeDto pitchTimeDto = pitchTimeDao.getBySubPitchIdAndTimeSlotId(bookingDto.getSubPitchId(),bookingDto.getTimeSlotId());
        SubPitchDto subPitchDto = subPitchDao.getSubPitchById(bookingDto.getSubPitchId());
        PitchDto pitchDto = pitchDao.getPitchById(subPitchDto.getPitchId(), true);
        List<ImageDto> imageDtoList = imageDao.getAllByPitchId(pitchDto.getId());
        UserDto userDto = userDao.getUserById(bookingDto.getUserId());
        UserDto managerDto = userDao.getUserById(pitchDto.getManagerId());

        PitchResponse pitchResponse = PitchResponse.builder()
                .id(pitchDto.getId())
                .name(pitchDto.getName())
                .location(pitchDto.getLocation())
                .images(imageDtoList.stream().map(ImageDto::getName).toList())
                .manager(UserResponse.fromUserDto(managerDto))
                .build();

        SubPitchResponse subPitchResponse = SubPitchResponse.builder()
                .name(subPitchDto.getName())
                .pitchType(subPitchDto.getPitchType())
                .build();

        PitchTimeResponse pitchTimeResponse = PitchTimeResponse.builder()
                .startTime(pitchTimeDto.getStartTime())
                .endTime(pitchTimeDto.getEndTime())
                .price(pitchTimeDto.getPrice())
                .build();

        BookingResponse bookingResponse = BookingResponse.toBookingResponse(
                bookingDto, UserResponse.fromUserDto(userDto), pitchResponse, subPitchResponse, pitchTimeResponse
        );
        return bookingResponse;
    }

}
