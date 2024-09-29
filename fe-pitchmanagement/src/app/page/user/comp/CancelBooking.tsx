import React, { useState } from "react";
import { convertBookingStatus, formatTime } from "../../../util/FormatDate";
import { useAppDispatch } from "../../../store/hooks";
import { showOrHideSpinner } from "../../../reducer/SpinnerSlice";
import { BookingService } from "../../../service/BookingService";
import { toast } from "react-toastify";
import { error } from "console";
import { useNavigate } from "react-router-dom";

export default function CancelBooking(props: any) {
  const { selectBooking } = props;

  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const [note, setNote] = useState<string>();
  const [caseCancel, setCaseCancel] = useState("none");
  const [confirmPolicy, setConfirmPolicy] = useState(false);

  const tomorrow = new Date();
  tomorrow.setDate(tomorrow.getDate() + 1);
  tomorrow.setHours(0, 0, 0, 0);

  const onSubmitCancel =  async () => {
    dispatch(showOrHideSpinner(true));
    await BookingService.getInstance().cancelBooking(selectBooking.id, {
      case_cancel : caseCancel,
      note : note,
    })
    .then((response) => {
      if(response.data.status === 200){
        toast.success(response.data.message, {
          autoClose: 1500,
          position: "top-right",
        });
        window.location.reload();
      }
      dispatch(showOrHideSpinner(false));
    })
    .catch((error) => {
      console.log(error);
      dispatch(showOrHideSpinner(false));
    });
  }

  return (
    selectBooking && (
      <>
        <h4>Hủy sân</h4>
        <div className="row shadow rounded py-2">
          <div className="col-5">
            <h5>Thông tin sân :</h5>
            <div className="mx-2">
              <h6>{selectBooking?.pitch.name}</h6>
              <p>Địa điểm : {selectBooking?.pitch.location}</p>
            </div>
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
                    {selectBooking?.deposit.toLocaleString()} VND{" "}
                    {`${
                      selectBooking.status === "DEPOSIT_PAID"
                        ? convertBookingStatus(selectBooking.status)
                        : ""
                    } `}
                  </b>
                </p>
                <p>
                  Tổng tiền : {selectBooking?.pitch_time.price.toLocaleString()}{" "}
                  VND
                </p>
              </div>
            </div>
          </div>
          <div className="col-5">
            <div>
              <h5>Lý do hủy:</h5>
              <div className="form-check">
                <input
                  className="form-check-input"
                  type="radio"
                  name="flexRadioDefault"
                  id="flexRadioDefault1"
                  value={"weather"}
                  checked={caseCancel === "weather"}
                  onChange={(e) => {
                    setCaseCancel(e.target.value);
                  }}
                />
                <label
                  className="form-check-label cursor-pointer"
                  htmlFor="flexRadioDefault1"
                >
                  Thời tiết mưa ngập...
                </label>
              </div>
              <div className="form-check">
                <input
                  className="form-check-input"
                  type="radio"
                  name="flexRadioDefault"
                  id="flexRadioDefault2"
                  value={"refund"}
                  checked={caseCancel === "refund"}
                  onChange={(e) => {
                    setCaseCancel(e.target.value);
                  }}
                  disabled={
                    !(
                      new Date(selectBooking.booking_date).getTime() >=
                        tomorrow.getTime() &&
                      selectBooking.status === "DEPOSIT_PAID"
                    )
                  }
                />
                <label
                  className="form-check-label cursor-pointer"
                  htmlFor="flexRadioDefault2"
                >
                  Hủy hoàn cọc
                </label>
              </div>
              <div className="form-check">
                <input
                  className="form-check-input"
                  type="radio"
                  name="flexRadioDefault"
                  id="flexRadioDefault3"
                  value={"none"}
                  checked={caseCancel === "none"}
                  onChange={(e) => {
                    setCaseCancel(e.target.value);
                  }}
                />
                <label
                  className="form-check-label cursor-pointer"
                  htmlFor="flexRadioDefault3"
                >
                  Khác
                </label>
              </div>
            </div>
            <div className="col-12">
              <h5>Ghi chú:</h5>
              <textarea
                className="form-control w-100 mx-3"
                placeholder="Ghi chú..."
                value={note}
                onChange={(e) => setNote(e.target.value)}
              />
            </div>
            <div className="form-check mt-2">
              <input
                className="form-check-input cursor-pointer"
                type="checkbox"
                id="confirmPolicy"
                onChange={(e) => setConfirmPolicy(e.target.checked)}
                checked={confirmPolicy}
              />
              <label className="form-check-label" htmlFor="confirmPolicy">
                Xác nhận tuân thủ theo <a href="#">chính sách</a> của công ty
              </label>
            </div>
          </div>
        </div>
        <div className="d-flex justify-content-end mt-3 mx-2">
          <button
            className="btn btn-danger px-5"
            disabled={!confirmPolicy}
            onClick={onSubmitCancel}
          >
            Hủy
          </button>
        </div>
      </>
    )
  );
}
