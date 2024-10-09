import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import image1 from "../../assets/images/image1.png";
import image2 from "../../assets/images/image2.png";
import image3 from "../../assets/images/image3.png";
import image4 from "../../assets/images/image4.png";
import contactIcon from "../../assets/images/contactIcon.png";
import defaultSanBong from "../../assets/images/defaultSanBong.jpeg";
import defaultAvatar from "../../assets/images/defaultAvatar.jpg";
import { Image } from "primereact/image";
import { FaStar } from "react-icons/fa";
import { PitchService } from "../service/PitchService";
import { PitchResponse } from "../model/Pitch";

export default function Home() {
  const navigate = useNavigate();

  const [pitchList, setPitchList] = useState<[PitchResponse]>();
  useEffect(() => {
    const fetchApi = async () => {
      await PitchService.getInstance()
        .getAll({
          page_number: 1,
          limit: 8,
          star_range: 0,
          request_query: true,
        })
        .then((response) => {
          if (response.data.status === 200) {
            setPitchList(response.data.data.items);
          }
        });
    };
    fetchApi();
  }, []);

  return (
    <>
      <div
        className="mt-3"
        style={{
          height: "400px",
          backgroundColor: "#EBEBEB",
        }}
      >
        <div className="container">
          <div className="row">
            <div className="col-6 mt-5">
              <p
                className="m-0"
                style={{ fontSize: "40px", fontFamily: "short-stack" }}
              >
                Đặt sân dễ dàng
              </p>
              <p
                className="m-0"
                style={{ fontSize: "40px", fontFamily: "short-stack" }}
              >
                Chơi bóng thỏa sức
              </p>
              <p>Hệ thống sân bóng rộng khắp, đặt sân chỉ trong vài phút.</p>
              <Link
                to={"/pitches"}
                className="btn text-white rounded rounded-4 mt-3"
                style={{
                  fontFamily: "intel",
                  width: "393px",
                  height: "66px",
                  backgroundColor: "#28A745",
                  fontSize: "40px",
                }}
              >
                Đặt sân ngay
              </Link>
            </div>
            <div className="col-6 d-flex justify-content-end mt-3">
              <img src={image1} alt="" />
            </div>
          </div>
        </div>
      </div>
      <div className="container">
        <div className="d-flex justify-content-center flex-column py-3">
          <h3 className="text-center mb-4">Top sân đánh giá tốt</h3>
          <div className="row">
            {pitchList &&
              pitchList.map((pitch: PitchResponse) => (
                <Link
                  to={`/pitches/${pitch.id}`}
                  key={pitch.id}
                  className="col-3 mb-3 text-decoration-none"
                >
                  <div className="card" style={{ width: "18rem" }}>
                    <img
                      src={
                        process.env.REACT_APP_API_URL +
                        "/images/" +
                        pitch.images[0]
                      }
                      onError={(e) => {
                        const target = e.target as HTMLImageElement;
                        target.src = defaultSanBong;
                      }}
                      className="card-img-top"
                      alt="..."
                    />
                    <div className="card-body">
                      <h5 className="card-title">{pitch.name}</h5>
                      <div className="d-flex align-items-center">
                        <Image
                          src={
                            process.env.REACT_APP_API_URL +
                            "/images/" +
                            pitch.manager.avatar
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
                        <b>{pitch.manager.fullname}</b>
                      </div>
                      <p className="card-text m-0">
                        {" "}
                        Địa điểm:
                        {" " + pitch.location}
                      </p>
                      <p className="card-text ">
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
                      </p>
                    </div>
                  </div>
                </Link>
              ))}
          </div>
        </div>
        <div
          className="row mt-3"
          style={{
            height: "400px",
            backgroundColor: "#EBEBEB",
          }}
        >
          <div className="col-6 d-flex flex-column justify-content-center align-items-center">
            <img src={contactIcon} alt="" width={300} height={197.89} />
            <p style={{ width: "300px", textAlign: "center" }}>
              Chúng tôi mời quý đối tác hợp tác cùng hệ thống quản lý cho thuê
              sân bóng, tối ưu hóa vận hành và tăng hiệu quả tiếp cận khách
              hàng.
            </p>
          </div>
          <div className="col-6 d-flex flex-column justify-content-center align-items-center">
            <div
              className="d-flex flex-column justify-content-center align-items-center rounded"
              style={{
                width: "530px",
                height: "300px",
                backgroundColor: "#FFFFFF",
              }}
            >
              <h4 className="mb-3">Liên hệ hợp tác</h4>
              <div className="row mx-4">
                <div className="d-flex mb-2">
                  <div className="col-3">
                    <span>Họ và tên</span>
                  </div>
                  <input
                    type="text"
                    className="form-control bg-body-secondary"
                  />
                </div>
                <div className="d-flex mb-2">
                  <div className="col-3">
                    <span>Email</span>
                  </div>
                  <input
                    type="email"
                    className="form-control bg-body-secondary"
                  />
                </div>
                <div className="d-flex mb-2">
                  <div className="col-3">
                    <span>Số điện thoại</span>
                  </div>
                  <input
                    type="text"
                    className="form-control bg-body-secondary"
                  />
                </div>
                <div className="d-flex justify-content-end">
                  <button className="btn btn-success me-4">Gửi ngay</button>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div className="row mt-3 d-flex justify-content-around">
          <div className="col-12 my-3">
            <h3 className="text-center">Dịch vụ của chúng tôi</h3>
          </div>
          <div className="col-3 rounded d-flex align-items-center flex-column bg-body-tertiary rounded">
            <img src={image2} alt="" />
            <p className="text-center">
              Sân bóng đá mặt cỏ nhân tạo chất lượng cao, mang đến bề mặt thi
              đấu mượt mà, độ bền vượt trội.
            </p>
          </div>
          <div className="col-3 rounded d-flex align-items-center flex-column bg-body-tertiary rounded">
            <img src={image3} alt="" />
            <p className="text-center">
              Đặt lịch nhanh chóng giúp bạn tiết kiệm thời gian và luôn sẵn sàng
              cho những trận đấu hấp dẫn.
            </p>
          </div>
          <div className="col-3 rounded d-flex align-items-center flex-column bg-body-tertiary rounded">
            <img src={image4} alt="" />
            <p className="text-center">
              Đảm bảo an toàn sân bãi đạt chuẩn, mang đến sự yên tâm và thoải
              mái cho mọi người chơi.
            </p>
          </div>
        </div>
      </div>
    </>
  );
}
