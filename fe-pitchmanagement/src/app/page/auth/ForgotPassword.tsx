import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { Link, useNavigate } from "react-router-dom";
import { UserService } from "../../service/UserService";
import { useAppDispatch } from "../../store/hooks";
import { toast } from "react-toastify";
import { showOrHideSpinner } from "../../reducer/SpinnerSlice";
import { Dialog } from "primereact/dialog";

export default function ForgotPassword() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    watch,
    formState: { errors, touchedFields },
  } = useForm<{ email: string }>({ mode: "onTouched" });

  const [visible, setVisible] = useState(false);

  const onSubmit = async (data: any) => {
    dispatch(showOrHideSpinner(true));

    await UserService.getInstance()
      .forgotPassword(data.email)
      .then((response) => {
        if (response.data.status === 200) {
          setVisible(true);
          dispatch(showOrHideSpinner(false));
        }
      })
      .catch((error) => {
        toast.error(error.response.data.message, {
          position: "top-right",
          autoClose: 1500,
        });
        dispatch(showOrHideSpinner(false));
      });
  };
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
            <Link to="/login">Đăng nhập</Link>
            {" hoặc "}
            <Link to="/register">Đăng ký ngay</Link>
          </p>
        </div>
      </form>
      <Dialog
        visible={visible}
        style={{ width: "50vw" }}
        onHide={() => {
          if (!visible) return;
          setVisible(false);
          navigate("/login");
        }}
      >
        <div className="container text-center bg-body-secondary p-2">
          <img
            src="https://cdn4.iconfinder.com/data/icons/social-media-logos-6/512/112-gmail_email_mail-512.png"
            style={{ width: "200px" }}
            alt="Img email"
          />
          <h3>Email đã được gửi thành công</h3>
          <p>
            Chúng tôi đã gửi tin nhắn qua địa chỉ email <b>{watch("email")}</b> để xác
            thực tài khoản. Sau khi nhận email vui lòng thực hiện theo chỉ dẫn
            mà email đã cung cấp để thực hiện bước tiếp theo.
          </p>
        </div>
      </Dialog>
    </div>
  );
}
