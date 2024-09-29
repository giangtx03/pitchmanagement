import { Image } from "primereact/image";
import React, { useEffect, useState } from "react";
import defaultAvatar from "../../../../assets/images/defaultAvatar.jpg";
import defaultSanBong from "../../../../assets/images/defaultSanBong.jpeg";
import { FaStar } from "react-icons/fa";
import {
  convertBookingStatus,
  formatDate,
  formatTime,
} from "../../../util/FormatDate";
import { Dialog } from "primereact/dialog";
import { BookingResponse } from "../../../model/Booking";
import CreateBooking from "./CreateBooking";
import CancelBooking from "./CancelBooking";

export default function Booking(props: any) {
  const { booking, handleChangeTimer } = props;

  const [selectBooking, setSelectBooking] = useState<BookingResponse>();
  const [btn, setBtn] = useState(0);
  const [visible, setVisible] = useState(false);
  const [timeLeft, setTimeLeft] = useState(1800);
  let createdAtDate = new Date(booking.create_at);

  useEffect(() => {
    if (isNaN(createdAtDate.getTime())) {
      createdAtDate = new Date(`${booking.create_at}Z`);
      console.log(createdAtDate);
    }
    const timer = setInterval(() => {
      const now = Date.now();
      const timePassed = Math.floor((now - createdAtDate.getTime()) / 1000);
      const remainingTime = 1800 - timePassed;

      if (remainingTime <= 0) {
        clearInterval(timer);
        setTimeLeft(0);
      } else {
        setTimeLeft(remainingTime);
      }
      if(remainingTime == 0){
        handleChangeTimer();
      }
    }, 1000);

    return () => clearInterval(timer);
  }, [booking]);

  const formatTimeLeft = (seconds: any) => {
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;
    return `${minutes}:${remainingSeconds < 10 ? "0" : ""}${remainingSeconds}`;
  };

  return (
    <>
      <div className="col-2">
        <img
          src={
            process.env.REACT_APP_API_URL + "/images/" + booking.pitch.images[0]
          }
          onError={(e) => {
            const target = e.target as HTMLImageElement;
            target.src = defaultSanBong;
          }}
          style={{ height: "120px", width: "auto" }}
          className="card-img-top"
          alt="..."
        />
      </div>
      <div className="col-3">
        <div className="mx-2">
          <h5 className="card-title">{booking.pitch.name}</h5>
          <div className="d-flex align-items-center">
            <Image
              src={
                process.env.REACT_APP_API_URL +
                "/images/" +
                booking.pitch.manager.avatar
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
            <b>{booking.pitch.manager.fullname}</b>
          </div>
          <p className="card-text m-0">
            {" "}
            Địa điểm:
            {" " + booking.pitch.location}
          </p>
          <p className="card-text ">
            <b className="text-warning d-flex align-items-center">
              {booking.pitch.avg_star > 0 ? (
                <>
                  {booking.pitch.avg_star.toFixed(2)}
                  <FaStar />
                </>
              ) : (
                "chưa có đánh giá"
              )}
            </b>
          </p>
        </div>
      </div>
      <div className="col-5 p-0 d-flex justify-content-around">
        <div className="col-3">
          <p className="m-0">
            Tên sân: <b className="text-danger">{booking.sub_pitch.name}</b>
          </p>
          <p className="m-0">
            Loại sân:{" "}
            <b className="text-danger">{booking.sub_pitch.pitch_type}</b>
          </p>
          <p className="m-0">
            Khung giờ:{" "}
            <b className="text-danger">
              {formatTime(booking.pitch_time.start_time) +
                " - " +
                formatTime(booking.pitch_time.end_time)}
            </b>
          </p>
          <p className="m-0">
            Ngày đá: <b className="text-danger">{booking.booking_date}</b>
          </p>
        </div>
        <div className="col-3">
          <p className="m-0">
            Giá sân:{" "}
            <b className="text-success">
              {booking.pitch_time.price.toLocaleString() + " VND"}
            </b>
          </p>
          <p className="m-0">
            {" "}
            Tiền cọc:{" "}
            <b className="text-success">
              {booking.deposit.toLocaleString() + " VND"}
            </b>
          </p>
        </div>
        <div className="col-4">
          <p>Thời gian đặt: {formatDate(booking.create_at)}</p>
          {timeLeft !== 0 && timeLeft !== 1800 && booking.status === "PENDING" && (
            <p>
              Thời gian còn lại:{" "}
              <b className="text-danger">{formatTimeLeft(timeLeft)}</b>
            </p>
          )}
        </div>
      </div>
      <div className="col-2 p-0">
        <p className="m-0 p-0">Trạng thái:</p>
        <p className="border p-1 rounded bg-info text-dark text-center m-0">
          {convertBookingStatus(booking.status)}
        </p>
        {booking.status === "PENDING" && (
          <button
            className="btn btn-success mt-1 p-1 w-100"
            onClick={() => {
              setVisible(true);
              setBtn(0);
              setSelectBooking(booking);
            }}
          >
            Thanh toán cọc
          </button>
        )}
        {booking.status !== "CANCELLED" && (
          <button
            className="btn btn-danger mt-1 p-1 w-100"
            onClick={() => {
              setVisible(true);
              setBtn(1);
              setSelectBooking(booking);
            }}
          >
            Hủy
          </button>
        )}
      </div>
      <Dialog
        header={`${btn === 0 ? "Thanh toán" : "Hủy"}`}
        visible={visible}
        style={{ width: "60vw" }}
        onHide={() => {
          if (!visible) return;
          setVisible(false);
        }}
        dismissableMask={true}
        baseZIndex={100}
      >
        {btn === 0 ? (
          <CreateBooking selectBooking={selectBooking} />
        ) : (
          <CancelBooking selectBooking={selectBooking}></CancelBooking>
        )}
      </Dialog>
    </>
  );
}
