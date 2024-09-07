import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAppDispatch } from "../store/hooks";
import { useForm } from "react-hook-form";
import { LoginRequest } from "../model/User";

export default function Login() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    formState: { errors, touchedFields },
  } = useForm<LoginRequest>({ mode: "onTouched" });

  const onSubmit = (data : any) => {
    console.log(data);
  }

  return (
    <div
      className="m-auto"
      style={{
        width: "500px",
        backgroundColor: "ghostwhite",
        boxShadow: "2px 2px 5px rgba(0, 0, 0, 0.1)",
      }}
    >
      <h3 className="text-center mb-4">Đăng nhập</h3>
      <form onSubmit={handleSubmit(onSubmit)}>
        {/* Email input */}
        <div data-mdb-input-init="" className="form-outline mb-4">
          <label className="form-label" htmlFor="form2Example1">
            Email
          </label>
          <input
            type="text"
            id="loginName"
            {...register("email", {
              required: "Email không được để trống",
              pattern: {
                value: /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/,
                message: "Email không đúng định dạng",
              },
            })}
            className="form-control"
            placeholder="Nhập email"
          />
        </div>
        {touchedFields.email && errors.email && (
          <p className="text-danger">{errors.email.message}</p>
        )}
        {/* Password input */}
        <div data-mdb-input-init="" className="form-outline mb-4">
          <label className="form-label" htmlFor="form2Example2">
            Mật khẩu
          </label>
          <input
            type="password"
            {...register("password", {
              required: "Mật khẩu không được để trống",
            })}
            id="loginPassword"
            className="form-control"
            placeholder="Nhập mật khẩu"
          />
        </div>
        {touchedFields.password && errors.password && (
          <p className="text-danger">{errors.password.message}</p>
        )}
        {/* 2 column grid layout for inline styling */}
        <div className="row mb-4">
          <div className="col d-flex justify-content-center">
            {/* Checkbox */}
            <div className="form-check">
              <input
                className="form-check-input"
                type="checkbox"
                defaultValue=""
                id="form2Example31"
              />
              <label className="form-check-label" htmlFor="form2Example31">
                {" "}
                Nhớ mật khẩu{" "}
              </label>
            </div>
          </div>
          <div className="col">
            {/* Simple link */}
            <a href="#!">Quên mật khẩu?</a>
          </div>
        </div>
        {/* Submit button */}
        <button
          type="submit"
          style={{ width: "100%" }}
          className="btn btn-primary btn-block mb-4"
        >
          Đăng nhập
        </button>
        {/* Register buttons */}
        <div className="text-center">
          <p>
            Chưa có tài khoản? <Link to="/register">Đăng ký ngay</Link>
          </p>
        </div>
      </form>
    </div>
  );
}
