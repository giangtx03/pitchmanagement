import { Button } from "primereact/button";
import {  useNavigate } from "react-router-dom";

export default function NotFound() {
  const navigate = useNavigate();
  return (
    <div className="d-flex align-items-center justify-content-center vh-100">
      <div className="text-center">
        <h1 className="display-1 fw-bold">404</h1>
        <p className="fs-3">
          {" "}
          <span className="text-danger">Xảy ra lỗi!</span> Trang không tìm thấy.
        </p>
        <p className="lead">Trang bạn xem không tồn tại.</p>
        <Button onClick={() => navigate("/home")}>
          Về trang chủ
        </Button>
      </div>
    </div>
  );
}