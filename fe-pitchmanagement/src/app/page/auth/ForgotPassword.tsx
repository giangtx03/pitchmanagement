import React from "react";
import { useForm } from "react-hook-form";
import { Link } from "react-router-dom";

export default function ForgotPassword() {
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors, touchedFields },
  } = useForm<{ email: string }>({ mode: "onTouched" });

  const onSubmit = async (data: any) => {};
  return (
    <div
      className="d-flex flex-column m-auto justify-content-center align-items-center p-5 bg-light shadow"
      style={{ maxWidth: "400px" }}
    >
      <h3 className="mb-4">Quên mật khẩu</h3>
      <form onSubmit={handleSubmit(onSubmit)} className="w-100">
        {/* Email input */}
        <div className="form-outline mb-4">
          <label className="form-label" htmlFor="loginName">
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
          {touchedFields.email && errors.email && (
            <p className="text-danger mt-2">{errors.email.message}</p>
          )}
        </div>

        {/* Submit button */}
        <button type="submit" className="btn btn-primary btn-block mb-4 w-100">
          Tìm kiếm
        </button>

        {/* Register link */}
        <div className="text-center">
          <p>
            <Link to="/login">Đăng nhập</Link>{" hoặc "}
            <Link to="/register">Đăng ký ngay</Link>
          </p>
        </div>
      </form>
    </div>
  );
}
