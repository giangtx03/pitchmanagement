import React, { useState } from "react";
import { useAppDispatch } from "../../../store/hooks";
import { showOrHideSpinner } from "../../../reducer/SpinnerSlice";
import { PaymentService } from "../../../service/PaymentService";
import { toast } from "react-toastify";
import { formatTime } from "../../../util/FormatDate";
import { Image } from "primereact/image";
import defaultAvatar from "../../../../assets/images/defaultAvatar.jpg";

export default function CreateBooking(props: any) {
  const dispatch = useAppDispatch();

  const { selectBooking } = props;

  const [note, setNote] = useState<string>();

  const handelPayDeposit = async () => {
    dispatch(showOrHideSpinner(true));
    await PaymentService.getInstance()
      .payBooking({
        booking_id: selectBooking?.id,
        note: note,
        amount: selectBooking?.deposit,
        payment_type: "DEPOSIT",
      })
      .then((response) => {
        if (response.data.status === 200) {
          window.location.href = response.data.data;
        }
        dispatch(showOrHideSpinner(false));
      })
      .catch((error) => {
        toast.error(error.response.data.message, {
          position: "top-right",
          autoClose: 1500,
        });
        dispatch(showOrHideSpinner(false));
      });
  };

  return (
    selectBooking && (
      <>
        <h4>Thanh toán tiền cọc</h4>
        <div className="row shadow rounded py-2">
          <div className="col-5">
            <h5>Thông tin sân :</h5>
            <div className="mx-2">
              <h6>{selectBooking?.pitch.name}</h6>
              <p>Địa điểm : {selectBooking?.pitch.location}</p>
            </div>
            <h5>Thông tin chủ sân</h5>
            <div className="mx-2">
              <div className="d-flex align-items-center">
                <Image
                  src={
                    process.env.REACT_APP_API_URL +
                    "/images/" +
                    selectBooking?.pitch.manager.avatar
                  }
                  alt="User Avatar"
                  width="30"
                  height="30"
                  onError={(e) => {
                    const target = e.target as HTMLImageElement;
                    target.src = defaultAvatar;
                  }}
                  style={{
                    borderRadius: "50%",
                    overflow: "hidden",
                    objectFit: "cover",
                  }}
                />
                <b>{selectBooking?.pitch.manager.fullname}</b>
              </div>
              <p className="m-0">
                Số điện thoại: {selectBooking?.pitch.manager.phone_number}
              </p>
              <p className="mb-1">
                Email: {selectBooking?.pitch.manager.email}
              </p>
            </div>
          </div>
          <div className="col-5">
            <h5>Thông tin đặt</h5>
            <div className="mx-2">
              <p className="m-0">
                Sân :{" "}
                <b className="text-danger">{selectBooking?.sub_pitch.name}</b>
              </p>
              <p className="m-0">
                Khung giờ :{" "}
                <b className="text-danger">
                  {formatTime(selectBooking?.pitch_time.start_time) +
                    " - " +
                    formatTime(selectBooking?.pitch_time.end_time)}
                </b>
              </p>
              <p>
                Ngày đá:{" "}
                <b className="text-danger">{selectBooking?.booking_date}</b>
              </p>
            </div>
            <div>
              <h5>Thông tin giá:</h5>
              <div className="mx-2">
                <p className="m-0">
                  Tiền cọc :{" "}
                  <b className="text-success">
                    {selectBooking?.deposit.toLocaleString()} VND
                  </b>
                </p>
                <p>
                  Tổng tiền : {selectBooking?.pitch_time.price.toLocaleString()}{" "}
                  VND
                </p>
              </div>
            </div>
          </div>
          <div className="col-12 d-flex align-items-start">
            <h5>Ghi chú:</h5>
            <textarea
              className="form-control w-50 mx-3"
              placeholder="Ghi chú..."
              value={note}
              onChange={(e) => setNote(e.target.value)}
            />
          </div>
        </div>
        <div className="d-flex justify-content-end mt-3 mx-2">
          <button className="btn btn-success" onClick={handelPayDeposit}>
            Thanh toán
          </button>
        </div>
      </>
    )
  );
}
