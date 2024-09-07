import React from "react";
import { Link } from "react-router-dom";

export default function Header() {
  return (
    <header className="d-flex flex-wrap align-items-center justify-content-center justify-content-md-between py-3 mb-4 border-bottom">
      <a
        href="/"
        className="d-flex align-items-center col-md-3 mb-2 mb-md-0 text-dark text-decoration-none"
      >
        <img
          src="https://www.svgrepo.com/show/51/soccer-ball.svg"
          width={50}
          alt=""
        />
      </a>
      <ul className="nav col-12 col-md-auto mb-2 justify-content-center mb-md-0">
        <li>
          <a href="#" className="nav-link px-2 link-secondary">
            Trang chủ
          </a>
        </li>
        <li>
          <a href="#" className="nav-link px-2 link-dark">
            Sân bóng
          </a>
        </li>
        <li>
          <a href="#" className="nav-link px-2 link-dark">
            Điều khoản
          </a>
        </li>
        <li>
          <a href="#" className="nav-link px-2 link-dark">
            Liên hệ
          </a>
        </li>
        <li>
          <a href="#" className="nav-link px-2 link-dark">
            Giới thiệu
          </a>
        </li>
      </ul>
      <div className="col-md-3 text-end">
        <Link to={"/login"} type="button" className="btn btn-outline-primary me-2">
          Đăng nhập
        </Link>
        <Link to={"/register"} type="button" className="btn btn-primary">
          Đăng ký
        </Link>
      </div>
    </header>
  );
}
