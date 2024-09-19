import React from "react";
import { NavLink } from "react-router-dom";
import { BsChevronDown } from "react-icons/bs";
import { useAppDispatch } from "../../../store/hooks";
import { logout } from "../../../reducer/UserSlice";

export default function SideBar() {
  const dispatch = useAppDispatch();

  return (
    <div
      className="flex-shrink-0 p-3 bg-white d-flex justify-content-between flex-column"
      style={{ borderRight: "1px solid gray", minHeight: "500px" }}
    >
      <ul className="list-unstyled ps-0">
        <li className="mb-1">
          <button
            className="btn btn-toggle text-start btn-secondary align-items-center rounded collapsed mb-1"
            data-bs-toggle="collapse"
            data-bs-target="#home-collapse"
            aria-expanded="true"
            style={{ width: "100%" }}
          >
            <BsChevronDown className="me-2 fs-5" />
            Thông tin cá nhân
          </button>
          <div className="collapse show" id="home-collapse">
            <ul
              className="btn-toggle-nav list-unstyled fw-normal pb-1 small"
              style={{ textAlign: "right" }}
            >
              <li>
                <NavLink
                  to="/users/profile"
                  className="btn text-start rounded mb-1"
                  style={{ width: "90%" }}
                >
                  Chỉnh sửa thông tin
                </NavLink>
              </li>
              <li>
                <NavLink
                  to="/users/change-password"
                  className="btn text-start rounded mb-1"
                  style={{ width: "90%" }}
                >
                  Đổi mật khẩu
                </NavLink>
              </li>
            </ul>
          </div>
        </li>
        <li className="mb-1">
          <NavLink
            to="/users/bookings"
            className="btn btn-secondary text-start align-items-center rounded"
            style={{ width: "100%" }}
          >
            Lịch sử đặt sân
          </NavLink>
        </li>
        <li className="mb-1">
          <NavLink
            to="/users/payments"
            className="btn btn-secondary text-start align-items-center rounded"
            style={{ width: "100%" }}
          >
            Danh sách hóa đơn
          </NavLink>
        </li>
      </ul>
      <button
        className="btn btn-secondary align-items-center rounded"
        style={{ width: "100%" }}
        onClick={() => dispatch(logout())}
      >
        Đăng xuất
      </button>
    </div>
  );
}
