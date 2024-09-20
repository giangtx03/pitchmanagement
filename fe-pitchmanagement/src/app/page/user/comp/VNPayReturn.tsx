import React, { useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { PaymentService } from "../../../service/PaymentService";
import { toast } from "react-toastify";
import { useAppDispatch } from "../../../store/hooks";
import { showOrHideSpinner } from "../../../reducer/SpinnerSlice";

export default function VNPayReturn() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  // Lấy thông tin từ URL
  const location = useLocation();

  // Sử dụng URLSearchParams để lấy query parameters
  const queryParams = new URLSearchParams(location.search);

  // Lấy từng tham số
  const bookingId = queryParams.get("booking_id");
  const vnpAmount = queryParams.get("vnp_Amount");
  const vnpBankCode = queryParams.get("vnp_BankCode");
  const vnpOrderInfo = queryParams.get("vnp_OrderInfo");
  const vnpResponseCode = queryParams.get("vnp_ResponseCode");
  const note = queryParams.get("note");
  const paymentType = queryParams.get("payment_type");
  const vnpTransactionStatus = queryParams.get("vnp_TransactionStatus");

  useEffect(() => {
    const fetchApi = async () => {
      await PaymentService.getInstance()
        .confirmPayment({
          booking_id: bookingId,
          amount: vnpAmount,
          vnp_BankCode: vnpBankCode,
          vnp_OrderInfo: vnpOrderInfo,
          vnp_ResponseCode: vnpResponseCode,
          note: note,
          payment_type: paymentType,
          vnp_TransactionStatus: vnpTransactionStatus,
        })
        .then((response) => {
          if (response.data.status === 200) {
            toast.success(response.data.message, {
              position: "top-right",
              autoClose: 1500,
            });
            dispatch(showOrHideSpinner(false));
          }
        })
        .catch((error) => {
          toast.error(error.response.data.message, {
            position: "top-right",
            autoClose: 1500,
          });
          dispatch(showOrHideSpinner(false));
        });
    };

    dispatch(showOrHideSpinner(true));
    fetchApi();
  }, []);

  return (
    <div className="spinner-border text-primary" role="status">
      <span className="visually-hidden">Đang xử lý thanh toán...</span>
    </div>
  );
}
