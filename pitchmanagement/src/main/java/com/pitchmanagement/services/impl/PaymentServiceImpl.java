package com.pitchmanagement.services.impl;

import com.pitchmanagement.configs.VNPayConfig;
import com.pitchmanagement.constants.BookingStatus;
import com.pitchmanagement.daos.BookingDao;
import com.pitchmanagement.daos.PaymentDao;
import com.pitchmanagement.daos.UserDao;
import com.pitchmanagement.dtos.BookingDto;
import com.pitchmanagement.dtos.PaymentDto;
import com.pitchmanagement.dtos.UserDto;
import com.pitchmanagement.models.requests.payment.VNPayRequest;
import com.pitchmanagement.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final BookingDao bookingDao;
    private final UserDao userDao;
    private final PaymentDao paymentDao;
    @Value("${frontend.api}")
    private String frontEndApi;
    @Override
    public String createPayment(VNPayRequest request) throws Exception {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
        String orderType = "order-type";

        BookingDto bookingDto = Optional.ofNullable(bookingDao.getBookingById(request.getBookingId()))
                .orElseThrow(() -> new NotFoundException("Đơn đặt không hợp lệ"));

        String orderInfor = request.getPaymentType() + " id san: " + bookingDto.getSubPitchId() + " id khung gio: " + bookingDto.getSubPitchId();

        Map<String, String> vnp_Params = new HashMap<>();

        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf((int)(request.getAmount()*100)));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfor);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = "vn";
        vnp_Params.put("vnp_Locale", locate);

        String noteEncoded = !(request.getNote() == null) ? URLEncoder.encode(request.getNote(), StandardCharsets.UTF_8.toString()) : "";

        String urlReturn = frontEndApi + VNPayConfig.vnp_ReturnUrl
                + "?note="+noteEncoded
                + "&booking_id="+ request.getBookingId()
                + "&payment_type="+ request.getPaymentType();
        vnp_Params.put("vnp_ReturnUrl", urlReturn);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                try {
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

        return paymentUrl;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void vnpayReturn(Long bookingId, int amount, String note,String paymentType, String bankCode, String orderInfo, String responseCode, String transactionStatus) throws Exception {
        BookingDto bookingDto = Optional.ofNullable(bookingDao.getBookingById(bookingId))
                .orElseThrow(() -> new NotFoundException("Đơn đặt không hợp lệ"));

        if(!"00".equals(responseCode)){
            throw new RuntimeException("Thanh toán thất bại!");
        }
        switch (transactionStatus) {
            case "01" -> throw new RuntimeException("Giao dịch chưa hoàn tất!");
            case "02" -> throw new RuntimeException("Giao dịch bị lỗi!");
            case "04" ->
                    throw new RuntimeException("Giao dịch đảo (Khách hàng đã bị trừ tiền tại Ngân hàng nhưng GD chưa thành công ở VNPAY)!");
            case "05" -> throw new RuntimeException("VNPAY đang xử lý giao dịch này (GD hoàn tiền)!");
            case "06" -> throw new RuntimeException("VNPAY đã gửi yêu cầu hoàn tiền sang Ngân hàng (GD hoàn tiền)!");
            case "07" -> throw new RuntimeException("Giao dịch bị nghi ngờ gian lận!");
            case "09" -> throw new RuntimeException("GD Hoàn trả bị từ chối!");
        }

        PaymentDto paymentDto = PaymentDto.builder()
                .paymentMethod("VNPAY")
                .paymentType(paymentType)
                .note(note)
                .bookingId(bookingId)
                .amount((float)amount)
                .createAt(LocalDateTime.now())
                .build();

        paymentDao.insertPayment(paymentDto);
        bookingDto.setStatus(BookingStatus.DEPOSIT_PAID.toString());
        bookingDao.updateBooking(bookingDto);
    }
}
