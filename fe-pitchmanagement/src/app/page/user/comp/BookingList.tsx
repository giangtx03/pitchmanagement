import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { BookingService } from "../../../service/BookingService";
import { useAppDispatch } from "../../../store/hooks";
import { showOrHideSpinner } from "../../../reducer/SpinnerSlice";
import { BookingResponse } from "../../../model/Booking";
import { toast } from "react-toastify";
import { useLocation } from "react-router-dom";
import Booking from "./Booking";

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

  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const message = queryParams.get("message");

  const [data, setData] = useState<any>();
  const [search, setSearch] = useState<SearchModel>({
    keyword: "",
    limit: 3,
    pageNumber: 1,
    status: "",
    timer: 0,
  });
  useEffect(() => {
    if (message === "Thanh toán thành công!") {
      toast.success(message, {
        position: "top-right",
        autoClose: 1500,
      });
    }
    else{
      toast.error(message, {
        position: "top-right",
        autoClose: 1500,
      });
    }
  }, [message]);

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
  }, [search.timer]);

  const statuses = [
    { label: "Tất cả", value: "", color: "btn-info" },
    { label: "Đang xử lý", value: "PENDING", color: "btn-info" },
    { label: "Đã cọc", value: "DEPOSIT_PAID", color: "btn-info" },
    { label: "Hoàn thành", value: "COMPLETED", color: "btn-info" },
    { label: "Đã hủy", value: "CANCELLED", color: "btn-info" },
  ];

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
            <Booking booking={booking} handleChangeTimer={() => setSearch({...search, timer: Date.now()})} />
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
      </div>
    )
  );
}
