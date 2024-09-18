import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { BookingService } from "../../../service/BookingService";
import { useAppDispatch } from "../../../store/hooks";
import { showOrHideSpinner } from "../../../reducer/SpinnerSlice";
import { BookingResponse } from "../../../model/Booking";

export default function BookingList() {
  const userDetail = useSelector((state: any) => state.user.userDetail);

  const dispatch = useAppDispatch();
  const [data, setData] = useState<any>();

  useEffect(() => {
    const fetchApi = async () => {
      await BookingService.getInstance()
        .getBookingByUserId(userDetail.id)
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
  }, [userDetail.id]);

  return (
    data && (
      <div>
        <h4>Danh sách đơn đặt sân:</h4>
        <p>Tổng số đơn: {data.total_items} </p>
        {data.items.map((booking: BookingResponse) => (
          <h2 key={booking.id}>{booking.id}</h2>
        ))}
      </div>
    )
  );
}
