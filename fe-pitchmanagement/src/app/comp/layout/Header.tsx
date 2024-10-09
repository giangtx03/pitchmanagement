import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { Link, NavLink, useNavigate } from "react-router-dom";
import { useAppDispatch } from "../../store/hooks";
import { DecodedToken, UserResponse } from "../../model/User";
import { decodeToken } from "react-jwt";
import { TokenService } from "../../service/TokenService";
import { UserService } from "../../service/UserService";
import { login, logout } from "../../reducer/UserSlice";
import iconLogo from "../../../assets/images/iconLogo.png";
import { Avatar } from "primereact/avatar";
import { BiUser } from "react-icons/bi";
import { BiBell } from "react-icons/bi";

export default function Header() {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();

  const isAuthenticated = useSelector(
    (state: any) => state.user.isAuthenticated
  );
  const [user, setUser] = useState<UserResponse | null>(null);
  const userDetail = useSelector((state: any) => state.user.userDetail);

  useEffect(() => {
    const fetchUserDetails = async () => {
      const token = TokenService.getInstance().getToken(); // Retrieve the token first
      const decode = decodeToken<DecodedToken>(token);

      if (decode) {
        try {
          const response = await UserService.getInstance().getUserDetails(
            decode.user_id
          );
          if (response.data.status === 200) {
            dispatch(login(response.data.data));
          }
        } catch (error) {
          console.error(error);
          dispatch(logout());
          navigate("/login");
        }
      }
    };

    fetchUserDetails();
  }, [TokenService.getInstance().getToken()]);

  useEffect(() => {
    setUser(userDetail);
  }, [userDetail]);

  return (
    <header className="fixed-top bg-light p-0">
      <div className="container d-flex align-items-center justify-content-center justify-content-md-between py-3 ">
        <div className="d-flex align-items-center col-md-3 mb-2 mb-md-0 text-dark text-decoration-none">
          <img src={iconLogo} width={50} alt="" />
        </div>
        <ul className="nav col-12 col-md-auto mb-2 justify-content-center mb-md-0">
          <li>
            <NavLink to="/home" className="nav-link px-2 link-dark">
              TRANG CHỦ
            </NavLink>
          </li>
          <li>
            <NavLink to="/pitches" className="nav-link px-2 link-dark">
              SÂN BÓNG
            </NavLink>
          </li>
          <li>
            <NavLink to="" className="nav-link px-2 link-dark">
              LIÊN HỆ
            </NavLink>
          </li>
          <li>
            <NavLink to="" className="nav-link px-2 link-dark">
              CHÍNH SÁCH
            </NavLink>
          </li>
        </ul>
        <div className="col-md-3 text-end">
          {isAuthenticated && user ? (
            <div className="d-flex justify-content-end align-items-center">
              <button className="btn" style={{ border: "none" }}>
                <BiBell style={{ fontSize: "24px" }} />
              </button>
              <button
                className="btn d-flex align-items-center"
                onClick={() => navigate("/users")}
              >
                <Avatar
                  image={
                    user.avatar
                      ? process.env.REACT_APP_API_URL + `/images/${user.avatar}`
                      : undefined
                  }
                  icon={
                    !user.avatar || user.avatar.length === 0 ? (
                      <BiUser style={{ fontSize: "24px" }} />
                    ) : undefined
                  }
                  shape="circle"
                />
                <p className="m-0 ms-1 font-semibold">{user.fullname}</p>
              </button>
            </div>
          ) : (
            <>
              <NavLink to={"/register"} type="button" className="btn me-2">
                ĐĂNG KÝ
              </NavLink>
              <NavLink to={"/login"} type="button" className="btn">
                ĐĂNG NHẬP
              </NavLink>
            </>
          )}
        </div>
      </div>
    </header>
  );
}
