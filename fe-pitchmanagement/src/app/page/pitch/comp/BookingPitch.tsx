import React, { useState } from "react";
import { PitchTimeResponse, SubPitchResponse } from "../../../model/Pitch";
import { formatTime } from "../../../util/FormatDate";

export default function BookingPitch(props: any) {
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

  return (
    <div>
      <div>
        <h4>{pitch.name}</h4>
        <h6>Loại sân: {selectSubPitch?.pitch_type}</h6>
        <h6>
          Giá sân:{" "}
          <b className="text-success">
            {selectPitchTime?.price.toLocaleString()} {" VND"}
          </b>
        </h6>
        <div className="d-flex align-items-center">
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
              <th className="border" key={index}>
                {daysOfWeek[date.getDay()]}
                <br />
                {date.toLocaleDateString()}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {selectSubPitch?.pitch_times.map((ptime) => (
            <tr
              key={ptime.start_time}
              onClick={() => setSelectPitchTime(ptime)}
              className="cursor-pointer"
            >
              <th>
                <input
                  className="form-check-input"
                  type="radio"
                  id={`pitch_time_${ptime.start_time}`}
                  name="pitch_time"
                  onClick={() => setSelectPitchTime(ptime)}
                  checked={ptime.start_time == selectPitchTime?.start_time}
                  style={{ display: "none" }}
                />
                <label
                  htmlFor={`pitch_time_${ptime.start_time}`}
                  className="form-check-label px-4"
                >
                  {formatTime(ptime.start_time) +
                    " - " +
                    formatTime(ptime.end_time)}
                </label>
              </th>
              {ptime.schedules.map((schedule, index) => (
                <td
                  key={index}
                  className={`${schedule == "OPENED" ? "" : "bg-warning"}`}
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
