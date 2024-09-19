import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { BookingService } from "../../../service/BookingService";
import { useAppDispatch } from "../../../store/hooks";
import { showOrHideSpinner } from "../../../reducer/SpinnerSlice";
import { BookingResponse } from "../../../model/Booking";
import defaultAvatar from "../../../../assets/images/defaultAvatar.jpg";
import defaultSanBong from "../../../../assets/images/defaultSanBong.jpeg";
import { Image } from "primereact/image";
import { FaStar } from "react-icons/fa";
import {
  convertBookingStatus,
  formatDate,
  formatTime,
} from "../../../util/FormatDate";
import { toast } from "react-toastify";
import { Dialog } from "primereact/dialog";

type SearchModel = {
  keyword: string;
  limit: number;
  pageNumber: number;
  status: string;
  timer: number;
};

export default function BookingList() {
  const userDetail = useSelector((state: any) => state.user.userDetail);

  const dispatch = useAppDispatch();
  const [data, setData] = useState<any>();
  const [search, setSearch] = useState<SearchModel>({
    keyword: "",
    limit: 3,
    pageNumber: 1,
    status: "",
    timer: 0,
  });
  const [visible, setVisible] = useState(false);

  useEffect(() => {
    const fetchApi = async () => {
      await BookingService.getInstance()
        .getBookingByUserId(userDetail.id, {
          keyword: search.keyword,
          limit: search.limit,
          page_number: search.pageNumber,
          status: search.status,
        })
        .then((response) => {
          // console.log(response);
          // console.log(search);
          if (response.data.status === 200) {
            setData(response.data.data);
            dispatch(showOrHideSpinner(false));
          }
        })
        .catch((error) => {
          dispatch(showOrHideSpinner(false));
        });
    };

    dispatch(showOrHideSpinner(true));
    fetchApi();
  }, [userDetail.id, search.timer]);

  const statuses = [
    { label: "Tất cả", value: "", color: "btn-info" },
    { label: "Đang xử lý", value: "PENDING", color: "btn-info" },
    { label: "Đã cọc", value: "DEPOSIT_PAID", color: "btn-info" },
    { label: "Hoàn thành", value: "COMPLETED", color: "btn-info" },
    { label: "Đã hủy", value: "CANCELLED", color: "btn-info" },
  ];

  const [note, setNote] = useState<string>();
  const [selectBooking, setSelectBooking] = useState<BookingResponse>();

  const handelPayDeposit = async () => {
    console.log(userDetail.id ,selectBooking?.id, note ,selectBooking?.deposit, "DEPOSIT");

    dispatch(showOrHideSpinner(true));
    await BookingService.getInstance()
      .payBooking({
        user_id: userDetail.id,
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
    data && (
      <div className="mx-2">
        <h4>Danh sách đơn đặt:</h4>
        <div className="d-flex input-group w-50 m-3">
          <input
            type="text"
            className="form-control"
            placeholder="Tìm kiếm theo tên sân, địa chỉ..."
            value={search.keyword}
            onChange={(e) => {
              setSearch({ ...search, keyword: e.target.value });
            }}
            onKeyDown={(e) => {
              if (e.key === "Enter") {
                setSearch({
                  ...search,
                  pageNumber: 1,
                  timer: Date.now(),
                });
              }
            }}
          />
          <button
            className="btn btn-primary"
            onClick={() => {
              setSearch({
                ...search,
                timer: Date.now(),
              });
            }}
          >
            Tìm kiếm
          </button>
        </div>
        <div className="d-flex mx-2 mb-3">
          {statuses.map((status) => (
            <a
              key={status.value}
              onClick={() =>
                setSearch({
                  ...search,
                  status: status.value,
                  pageNumber: 1,
                  timer: Date.now(),
                })
              }
              className={`btn mx-2 ${status.color} ${
                search.status === status.value ? "active" : ""
              }`}
            >
              {status.label}
            </a>
          ))}
        </div>

        <p>Tổng số đơn đặt: {data.total_items} </p>
        {data.items.map((booking: BookingResponse) => (
          <div className="row mt-2 border rounded shadow p-3" key={booking.id}>
            <div className="col-2">
              <img
                src={
                  process.env.REACT_APP_API_URL +
                  "/images/" +
                  booking.pitch.images[0]
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
                  Tên sân:{" "}
                  <b className="text-danger">{booking.sub_pitch.name}</b>
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
              </div>
            </div>
            <div className="col-2 p-0">
              <p className="m-0 p-0">Trạng thái:</p>
              <p className="border p-1 rounded bg-info text-secondary text-center m-0">
                {convertBookingStatus(booking.status)}
              </p>
              {booking.status === "PENDING" && (
                <button
                  className="btn btn-success mt-1 p-1 w-100"
                  onClick={() => {
                    setVisible(true);
                    setSelectBooking(booking);
                  }}
                >
                  Thanh toán cọc
                </button>
              )}
              {booking.status !== "CANCELED" && (
                <button className="btn btn-danger mt-1 p-1 w-100">Hủy</button>
              )}
            </div>
          </div>
        ))}
        {data.items.length > 0 && (
          <div className="col-12 mt-3 d-flex justify-content-center">
            <nav aria-label="Page navigation">
              <ul className="pagination ">
                <li className="page-item">
                  <button
                    className="page-link"
                    onClick={() => {
                      if (search.pageNumber > 1) {
                        setSearch({
                          ...search,
                          pageNumber: search.pageNumber - 1,
                          timer: Date.now(),
                        });
                      }
                    }}
                    aria-label="Previous"
                  >
                    <span aria-hidden="true">«</span>
                  </button>
                </li>
                {search.pageNumber === data.total_pages &&
                  data.total_pages > 2 && (
                    <li className="page-item">
                      <button
                        className="page-link"
                        onClick={() => {
                          setSearch({
                            ...search,
                            pageNumber: search.pageNumber - 2,
                            timer: Date.now(),
                          });
                        }}
                      >
                        {search.pageNumber - 2}
                      </button>
                    </li>
                  )}
                {search.pageNumber > 1 && (
                  <li className="page-item">
                    <button
                      className="page-link"
                      onClick={() => {
                        setSearch({
                          ...search,
                          pageNumber: search.pageNumber - 1,
                          timer: Date.now(),
                        });
                      }}
                    >
                      {search.pageNumber - 1}
                    </button>
                  </li>
                )}
                <li className="page-item">
                  <button className="page-link active">
                    {search.pageNumber}
                  </button>
                </li>
                {search.pageNumber < data.total_pages && (
                  <li className="page-item">
                    <button
                      className="page-link"
                      onClick={() => {
                        setSearch({
                          ...search,
                          pageNumber: search.pageNumber + 1,
                          timer: Date.now(),
                        });
                      }}
                    >
                      {search.pageNumber + 1}
                    </button>
                  </li>
                )}
                {search.pageNumber === 1 && data.total_pages > 2 && (
                  <li className="page-item">
                    <button
                      className="page-link"
                      onClick={() => {
                        setSearch({
                          ...search,
                          pageNumber: search.pageNumber + 2,
                          timer: Date.now(),
                        });
                      }}
                    >
                      {search.pageNumber + 2}
                    </button>
                  </li>
                )}
                <li className="page-item">
                  <button
                    className="page-link"
                    onClick={() => {
                      if (search.pageNumber < data.total_pages) {
                        setSearch({
                          ...search,
                          pageNumber: search.pageNumber + 1,
                          timer: Date.now(),
                        });
                      }
                    }}
                    aria-label="Next"
                  >
                    <span aria-hidden="true">»</span>
                  </button>
                </li>
              </ul>
            </nav>
          </div>
        )}
        <Dialog
          header="Thanh toán"
          visible={visible}
          style={{ width: "60vw" }}
          onHide={() => {
            if (!visible) return;
            setVisible(false);
          }}
          dismissableMask={true}
          baseZIndex={100}
        >
          {selectBooking && (
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
                      <b className="text-danger">
                        {selectBooking?.sub_pitch.name}
                      </b>
                    </p>
                    <p>
                      Khung giờ :{" "}
                      <b className="text-danger">
                        {formatTime(selectBooking?.pitch_time.start_time) +
                          " - " +
                          formatTime(selectBooking?.pitch_time.end_time)}
                      </b>
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
                        Tổng tiền :{" "}
                        {selectBooking?.pitch_time.price.toLocaleString()} VND
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
          )}
        </Dialog>
      </div>
    )
  );
}
