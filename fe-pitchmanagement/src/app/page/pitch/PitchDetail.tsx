import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import {
  PitchResponse,
  PitchTimeResponse,
  SubPitchResponse,
} from "../../model/Pitch";
import { PitchService } from "../../service/PitchService";
import { useAppDispatch } from "../../store/hooks";
import { showOrHideSpinner } from "../../reducer/SpinnerSlice";
import defaultSanBong from "../../../assets/images/defaultSanBong.jpeg";
import { FaStar } from "react-icons/fa";
import { Image } from "primereact/image";
import defaultAvatar from "../../../assets/images/defaultAvatar.jpg";
import { formatTime } from "../../util/FormatDate";
import ReviewList from "./comp/ReviewList";
import { Dialog } from "primereact/dialog";
import BookingPitch from "./comp/BookingPitch";
import { useSelector } from "react-redux";

export default function PitchDetail() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const isAuthenticated = useSelector(
    (state: any) => state.user.isAuthenticated
  );
  const { id } = useParams();

  const [pitch, setPitch] = useState<PitchResponse | any>();
  const [preview, setPreview] = useState<string | undefined>();
  const [selectedSubPitch, setSelectedSubPitch] =
    useState<SubPitchResponse | null>(null);
  const [selectedTime, setSelectedTime] = useState<PitchTimeResponse | null>(
    null
  );
  const [visible, setVisible] = useState(false);

  useEffect(() => {
    const fetchApi = async () => {
      await PitchService.getInstance()
        .getPitchById(id, { request_query: true })
        .then((response) => {
          // console.log(response);
          if (response.data.status === 200) {
            setPitch(response.data.data);
            if (response.data.data.images?.length > 0) {
              setPreview(response.data.data.images[0]);
            }
            setSelectedSubPitch(response.data.data.sub_pitches[0]);
            dispatch(showOrHideSpinner(false));
          }
        })
        .catch(() => {
          navigate("*");
          dispatch(showOrHideSpinner(false));
        });
    };
    dispatch(showOrHideSpinner(true));
    fetchApi();
  }, [id]);

  const handleSelectSubPitch = (sub_pitch: SubPitchResponse) => {
    setSelectedSubPitch(sub_pitch);
    setSelectedTime(null);
  };

  const handleSelectTime = (
    pitch_time: PitchTimeResponse,
    sub_pitch: SubPitchResponse
  ) => {
    setSelectedTime(pitch_time);
    setSelectedSubPitch(sub_pitch);
  };

  return pitch ? (
    <div>
      <div className="row shadow p-3">
        <div className="col-md-5 col-sm-12 ">
          <div className="row py-3 ">
            <div className="col-12 mb-1">
              <div
                className="lightbox position-relative"
                data-mdb-lightbox-init=""
              >
                <Image
                  src={
                    process.env.REACT_APP_API_URL +
                    "/images/" +
                    `${preview ? preview : pitch.images[0]}`
                  }
                  alt="Ảnh sân"
                  width="100%"
                  height="100%"
                  onError={(e) => {
                    const target = e.target as HTMLImageElement;
                    target.src = defaultSanBong;
                  }}
                  preview = {preview? true : false}
                />
              </div>
            </div>
            <div className="row m-auto overflow-auto d-flex flex-nowrap">
              {pitch.images.map((image: any, index: any) => {
                return (
                  <div className="col-3 mt-1" key={index} id={`image-${index}`}>
                    <button
                      className={
                        `p-0 ` +
                        `${
                          image == preview
                            ? "border border-3 border-info"
                            : "border-0"
                        }`
                      }
                      onClick={() => {
                        setPreview(image);
                      }}
                    >
                      <img
                        src={process.env.REACT_APP_API_URL + "/images/" + image}
                        alt={`Gallery image ${index}`}
                        className="active w-100"
                      />
                    </button>
                  </div>
                );
              })}
            </div>
          </div>
        </div>
        <div className="col-md-7 col-sm-12 mt-3">
          <div className="mx-3">
            <div className="d-flex justify-content-between align-items-center">
              <h4>{pitch.name}</h4>
              <button
                className="btn btn-success"
                onClick={() => {
                  if (isAuthenticated) setVisible(true);
                  else navigate("/login")
                }}
              >
                Đặt sân ngay
              </button>
            </div>
            <div className="card-text">
              <b className="text-warning d-flex align-items-center">
                {pitch.avg_star > 0 ? (
                  <>
                    {pitch.avg_star.toFixed(2)}
                    <FaStar />
                  </>
                ) : (
                  "chưa có đánh giá"
                )}
              </b>
              <p>Địa chỉ : {pitch.location}</p>
            </div>
            <div className="d-flex align-items-center mb-2">
              <Image
                src={
                  process.env.REACT_APP_API_URL +
                  "/images/" +
                  pitch.manager.avatar
                }
                alt="User Avatar"
                width="40"
                height="40"
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
              <b>{pitch.manager.fullname}</b>
            </div>
            <h5>
              Giá :{" "}
              {selectedSubPitch && !selectedTime
                ? `${Math.min(
                    ...selectedSubPitch.pitch_times.map((pt) => pt.price)
                  ).toLocaleString()} - ${Math.max(
                    ...selectedSubPitch.pitch_times.map((pt) => pt.price)
                  ).toLocaleString()}`
                : ""}
              {selectedTime && `${selectedTime.price.toLocaleString()}`}
              {" VND"}
            </h5>
            <div className="d-flex flex-column">
              {selectedSubPitch && (
                <div className="mt-3">
                  <p>Loại sân: {selectedSubPitch.pitch_type}</p>
                </div>
              )}
              <table className="table table-bordered table-hover">
                <thead className="table-light">
                  <tr>
                    <th style={{ width: "20%" }} scope="col">
                      Sân
                    </th>
                    <th style={{ width: "80%" }} scope="col">
                      Khung giờ
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {pitch.sub_pitches.map((sub_pitch: SubPitchResponse) => (
                    <tr key={sub_pitch.id}>
                      {/* Cột cho tên sân */}
                      <td className="align-middle d-flex justify-content-center">
                        <div className="form-check p-0">
                          <input
                            className="form-check-input"
                            value={sub_pitch.id}
                            type="radio"
                            id={`sub_pitch_${sub_pitch.id}`}
                            name="sub_pitch"
                            checked={selectedSubPitch?.id === sub_pitch.id}
                            onChange={() => handleSelectSubPitch(sub_pitch)}
                            style={{ display: "none" }}
                          />
                          <label
                            htmlFor={`sub_pitch_${sub_pitch.id}`}
                            className="btn border px-4"
                          >
                            {sub_pitch.name}
                          </label>
                        </div>
                      </td>

                      {/* Cột cho khung giờ */}
                      <td>
                        <div className="d-flex flex-wrap">
                          {sub_pitch.pitch_times.map(
                            (pitch_time: PitchTimeResponse, index) => (
                              <div className="form-check mx-2" key={index}>
                                <input
                                  className="form-check-input"
                                  type="radio"
                                  id={`pitch_time_${sub_pitch.id}_${index}`}
                                  name="pitch_time"
                                  onChange={() =>
                                    handleSelectTime(pitch_time, sub_pitch)
                                  }
                                  checked={selectedTime === pitch_time}
                                  style={{ display: "none" }}
                                />
                                <label
                                  className="btn border"
                                  htmlFor={`pitch_time_${sub_pitch.id}_${index}`}
                                >
                                  {formatTime(pitch_time.start_time)} -{" "}
                                  {formatTime(pitch_time.end_time)}
                                </label>
                              </div>
                            )
                          )}
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
      <div className="row shadow mt-3">
        <ReviewList pitchId={id} />
      </div>
      <Dialog
        header="Đặt sân"
        visible={visible}
        style={{ width: "80vw" }}
        onHide={() => {
          if (!visible) return;
          setVisible(false);
        }}
        dismissableMask={true}
        baseZIndex={100}
      >
        <BookingPitch pitch={pitch} />
      </Dialog>
    </div>
  ) : (
    <p> Không tìm thấy sân bóng </p>
  );
}
