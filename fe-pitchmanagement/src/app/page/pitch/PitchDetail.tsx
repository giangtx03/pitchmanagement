import React, { useEffect, useRef, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { PitchResponse } from "../../model/Pitch";
import { PitchService } from "../../service/PitchService";
import { useAppDispatch } from "../../store/hooks";
import { showOrHideSpinner } from "../../reducer/SpinnerSlice";
import defaultSanBong from "../../../assets/images/defaultSanBong.jpeg";
import { toast } from "react-toastify";

export default function PitchDetail() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id } = useParams();

  const [pitch, setPitch] = useState<PitchResponse | any>();
  const [preview, setPreview] = useState<string | undefined>();
  const [indexImg, setIndexImg] = useState<number>(0);
  const imageRowRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const fetchApi = async () => {
      await PitchService.getInstance()
        .getPitchById(id, { request_query: true })
        .then((response) => {
          console.log(response);
          if (response.data.status === 200) {
            setPitch(response.data.data);
            if (response.data.data.images?.length > 0) {
              setPreview(response.data.data.images[0]);
            }
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

  const handleNextImage = () => {
    let nextIndex = indexImg + 1;
    if (nextIndex >= pitch.images.length) {
      nextIndex = 0;
    }
    setIndexImg(nextIndex);
    setPreview(pitch.images[nextIndex]);

    // Tự động scroll theo khi đến ảnh cuối
    const selectedImage = document.getElementById(`image-${nextIndex}`);
    selectedImage?.scrollIntoView({ behavior: "smooth", inline: "center" });
  };

  const handlePrevImage = () => {
    let prevIndex = indexImg - 1;
    if (prevIndex < 0) {
      prevIndex = pitch.images.length - 1;
    }
    setIndexImg(prevIndex);
    setPreview(pitch.images[prevIndex]);

    // Tự động scroll theo khi quay lại ảnh trước
    const selectedImage = document.getElementById(`image-${prevIndex}`);
    selectedImage?.scrollIntoView({ behavior: "smooth", inline: "center" });
  };

  return pitch ? (
    <div className="shadow p-3 rounded">
      <div className="col-md-4 col-sm-12 ">
        <div className="row py-3 ">
          <div className="col-12 mb-1">
            <div
              className="lightbox position-relative"
              data-mdb-lightbox-init=""
            >
              <img
                src={
                  process.env.REACT_APP_API_URL +
                  "/images/" +
                  `${preview ? preview : pitch.images[0]}`
                }
                alt="Ảnh sân"
                className="w-100"
                onError={(e) => {
                  const target = e.target as HTMLImageElement;
                  target.src = defaultSanBong;
                }}
              />

              {/* Nút điều hướng trước */}
              <button
                className="btn fs-4 text-light position-absolute py-5"
                style={{
                  top: "50%",
                  left: "10px",
                  transform: "translateY(-50%)",
                }}
                onClick={handlePrevImage}
              >
                {"<"}
              </button>

              {/* Nút điều hướng sau */}
              <button
                className="btn fs-4 text-light position-absolute py-5"
                style={{
                  top: "50%",
                  right: "10px",
                  transform: "translateY(-50%)",
                }}
                onClick={handleNextImage}
              >
                {">"}
              </button>
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
                      setIndexImg(index);
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
    </div>
  ) : (
    <p> Không tìm thấy sân bóng </p>
  );
}
