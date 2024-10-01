import React from "react";
import { useNavigate } from "react-router-dom";
import biaSanBong from "../../assets/images/biaSanBong.jpg";
import iconSanBong from "../../assets/images/iconSanBong.jpg";
import iconDatLich from "../../assets/images/iconDatLich.jpg";

export default function Home() {
  const navigate = useNavigate();
  return (
    <>
      <div
        className="col-12 text-center mt-3"
        style={{
          backgroundImage: `url(${biaSanBong})`,
          backgroundSize: "cover",
          backgroundPosition: "center",
          minHeight: "60vh",
          padding: "50px 0",
          color: "#fff",
          display: "flex",
          flexDirection: "column",
          justifyContent: "center",
          alignItems: "center",
        }}
      >
        <div
          style={{
            backgroundColor: "rgba(255, 255, 255, 0.3)", // Nền màu trắng với độ mờ
            backdropFilter: "blur(10px)", // Hiệu ứng mờ nền
            padding: "20px",
            borderRadius: "10px", // Bo tròn các góc
            textAlign: "center",
            width: "50%",
          }}
        >
          <h3 className="text-primary">Hệ thống đặt sân bóng online</h3>
          <p className="text-dark">
            Hệ thống chuyên cung cấp sân bóng uy tín bậc nhất Hà Nội
          </p>
          <button
            onClick={() => {
              navigate("/pitches");
            }}
            className="btn btn-info text-light"
          >
            Tìm kiếm sân ngay
          </button>
        </div>
      </div>
      <div className="row mt-3 justify-content-around">
        <div
          className="col-lg-5 col-md-12"
          style={{
            padding: "20px",
            textAlign: "center",
          }}
        >
          <img
            src={iconSanBong}
            alt="Icon sân bóng"
            style={{
              width: "200px",
              height: "auto",
              display: "block",
              margin: "0 auto",
            }}
          />
          <h4>Tìm kiếm vị trí sân</h4>
          <p>
            Dữ liệu sân đấu dồi dào, liên tục cập nhật, giúp bạn dễ dàng tìm
            kiếm theo khu vực mong muốn
          </p>
        </div>
        <div
          className="col-lg-5 col-md-12"
          style={{
            padding: "20px",
            textAlign: "center",
          }}
        >
          <img
            src={iconDatLich}
            alt="Icon sân bóng"
            style={{
              width: "200px",
              height: "auto",
              display: "block",
              margin: "0 auto",
            }}
          />
          <h4>Đặt lịch online</h4>
          <p>
            Không cần đến trực tiếp, không cần gọi điện đặt lịch, bạn hoàn toàn
            có thể đặt sân ở bất kì đâu nếu có internet
          </p>
        </div>
      </div>
      <div
        className="row d-flex justify-content-center align-items-center mt-3 py-4 rounded shadow text-white"
        style={{ height: "200px", backgroundImage: `url(${biaSanBong})` }}
      >
        <div
          style={{
            backgroundColor: "rgba(255, 255, 255, 0.3)", // Nền màu trắng với độ mờ
            backdropFilter: "blur(10px)", // Hiệu ứng mờ nền
            padding: "20px",
            borderRadius: "10px", // Bo tròn các góc
            textAlign: "center",
            width: "50%",
          }}
        >
          <h3>Bạn có sân muốn hợp tác</h3>
          <p>
            Liên hệ ngay với chúng tôi qua email hoặc số điện thoại:
            pitch.manager.firms@gmail.com | 0123456789
          </p>
        </div>
      </div>
    </>
  );
}
