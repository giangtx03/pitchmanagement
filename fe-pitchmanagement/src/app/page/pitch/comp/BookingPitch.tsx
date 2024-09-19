import React, { useState } from "react";
import { PitchTimeResponse, SubPitchResponse } from "../../../model/Pitch";
import { formatTime } from "../../../util/FormatDate";
import { toast } from "react-toastify";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { useAppDispatch } from "../../../store/hooks";
import Swal from "sweetalert2";
import { showOrHideSpinner } from "../../../reducer/SpinnerSlice";
import { BookingService } from "../../../service/BookingService";

export default function BookingPitch(props: any) {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const userDetail = useSelector((state: any) => state.user.userDetail);
  const { pitch } = props;

  const daysOfWeek = [
    "Sunday",
    "Monday",
    "Tuesday",
    "Wednesday",
    "Thursday",
    "Friday",
    "Saturday",
  ];
  const [selectSubPitch, setSelectSubPitch] = useState<SubPitchResponse | null>(
    null
  );
  const [selectPitchTime, setSelectPitchTime] =
    useState<PitchTimeResponse | null>(null);
  const [selectedDateIndex, setSelectedDateIndex] = useState<number>(-1);
  const [note, setNote] = useState<string>();

  const today = new Date();
  const getNext7Days = () => {
    let next7Days = [];
    for (let i = 0; i < 7; i++) {
      let date = new Date(today);
      date.setDate(today.getDate() + i);
      next7Days.push(date);
    }
    return next7Days;
  };
  const next7Days = getNext7Days();

  const handleSubmitBooking = () => {
    if (selectedDateIndex === -1 || !selectPitchTime || !selectSubPitch) {
      toast.warn("Vui lòng chọn sân và thời gian phù hợp!", {
        position: "top-right",
        autoClose: 1500,
      });
    } else {
      Swal.fire({
        title: "Bạn có chắc chắn ?",
        text: "Xác nhận đặt sân!",
        icon: "success",
        showCancelButton: true,
        confirmButtonText: "Xác nhận",
        cancelButtonText: "Hủy",
        reverseButtons: true,
      }).then(async (result) => {
        if (result.isConfirmed) {
          dispatch(showOrHideSpinner(true));
          const date = new Date();
          date.setDate(today.getDate() + selectedDateIndex);
          const localDateString = date.toISOString().split("T")[0];
          // console.log(note);
          // console.log(localDateString);
          // console.log(selectPitchTime);
          // console.log(selectSubPitch);

          await BookingService.getInstance()
            .createBooking({
              user_id: userDetail.id,
              sub_pitch_id: selectSubPitch.id,
              time_slot_id: selectPitchTime.time_slot_id,
              booking_date: localDateString,
              note: note,
            })
            .then((response) => {
              if (response.data.status === 201) {
                toast.success(response.data.message, {
                  position: "top-right",
                  autoClose: 1500,
                });
                navigate("/pitches");
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
        }
      });
    }
  };

  return (
    <div>
      <div>
        <div className="d-flex justify-content-between align-items-center">
          <h4>{pitch.name}</h4>
          <button
            className="btn btn-success p-2 px-3 me-3"
            onClick={handleSubmitBooking}
          >
            Đặt sân
          </button>
        </div>
        <h6>Loại sân: {selectSubPitch?.pitch_type}</h6>
        <h6>
          Giá sân:{" "}
          <b className="text-success">
            {selectPitchTime?.price.toLocaleString()} {" VND"}
          </b>
        </h6>
        <div>
          <label htmlFor="">Ghi chú: </label>
          <textarea
            className="form-control w-50"
            value={note}
            onChange={(e) => setNote(e.target.value)}
            placeholder="Ghi chú..."
          />
        </div>
        <div className="d-flex mt-3 align-items-center">
          <h5>Danh sách sân: </h5>
          <div className="d-flex mx-2">
            {pitch.sub_pitches.map((sub_pitch: SubPitchResponse) => (
              <div key={sub_pitch.id} className="mx-1">
                <input
                  className="form-check-input"
                  type="radio"
                  id={`sub_pitch` + sub_pitch.id}
                  name={`sub_pitch2`}
                  style={{ display: "none" }}
                  checked={selectSubPitch == sub_pitch}
                  onChange={() => {
                    setSelectSubPitch(sub_pitch);
                    setSelectPitchTime(null);
                    setSelectedDateIndex(-1);
                  }}
                />
                <label
                  className="btn form-check-label border px-2 cursor-pointer"
                  style={{ minWidth: "80px" }}
                  htmlFor={`sub_pitch` + sub_pitch.id}
                >
                  {sub_pitch.name}
                </label>
              </div>
            ))}
          </div>
        </div>
      </div>
      <table className="table mt-3 table-bordered table-hover">
        <thead className="table-light">
          <tr>
            <th className="border"></th>
            {next7Days.map((date, index) => (
              <th
                className={`border ${
                  selectedDateIndex == index ? "bg-primary text-light" : ""
                }`}
                key={index}
              >
                {daysOfWeek[date.getDay()]}
                <br />
                {date.toLocaleDateString()}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {selectSubPitch?.pitch_times.map((ptime) => (
            <tr key={ptime.start_time} className="cursor-pointer">
              <th
                className={`${
                  ptime.start_time == selectPitchTime?.start_time
                    ? "bg-primary text-light"
                    : ""
                }`}
              >
                {formatTime(ptime.start_time) +
                  " - " +
                  formatTime(ptime.end_time)}
              </th>
              {ptime.schedules.map((schedule, index) => (
                <td
                  key={index}
                  className={`${schedule == "OPENED" ? "" : "bg-danger"} ${
                    selectPitchTime == ptime && selectedDateIndex == index
                      ? "bg-success text-light"
                      : ""
                  }`}
                  onClick={() => {
                    if (schedule == "OPENED") {
                      setSelectedDateIndex(index);
                      setSelectPitchTime(ptime);
                    }
                  }}
                >
                  {schedule == "OPENED" ? "Còn trống" : "Đã hết"}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
