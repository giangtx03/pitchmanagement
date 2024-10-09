import React, { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { Link, useNavigate } from "react-router-dom";
import { useAppDispatch } from "../store/hooks";
import { RegisterRequest } from "../model/User";
import { Dialog } from "primereact/dialog";
import SendEmail from "./auth/SendEmail";
import { showOrHideSpinner } from "../reducer/SpinnerSlice";
import { UserService } from "../service/UserService";
import { toast } from "react-toastify";

export default function Register() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    watch,
    setError,
    clearErrors,
    formState: { errors, touchedFields },
  } = useForm<RegisterRequest>({ mode: "onTouched" });

  const [visible, setVisible] = useState(false);

  const onSubmit = async (data: any) => {
    dispatch(showOrHideSpinner(true));

    await UserService.getInstance()
      .register({
        email: data.email,
        password: data.password,
        fullname: data.fullname,
        phone_number: data.phoneNumber,
      })
      .then((response) => {
        if (response.data.status === 201) {
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

  useEffect(() => {
    const retypePassword = watch("retypePassword");
    if (retypePassword && retypePassword !== watch("password")) {
      setError("retypePassword", {
        type: "manual",
        message: "Mật khẩu không khớp",
      });
    } else {
      clearErrors("retypePassword");
    }
  }, [watch("password"), watch, setError, clearErrors]);

  const handleInput = (event: any) => {
    event.target.value = event.target.value.replace(/[^0-9]/g, "");
  };

  return (
    <div
      className="m-auto shadow rounded p-3"
      style={{
        width: "500px",
        backgroundColor: "ghostwhite",
        boxShadow: "2px 2px 5px rgba(0, 0, 0, 0.1)",
      }}
    >
      <h3 className="text-center mb-4">Đăng ký</h3>
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
        <div data-mdb-input-init className="form-outline mb-4">
          <label className="form-label" htmlFor="registerFullname">
            Họ và tên
          </label>
          <input
            type="text"
            id="registerFullname"
            {...register("fullname", {
              required: "Tên không được để trống",
            })}
            className="form-control"
            placeholder="Nhập họ và tên"
          />
        </div>
        {touchedFields.fullname && errors.fullname && (
          <p className="text-danger">{errors.fullname.message}</p>
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
              minLength: {
                value: 8,
                message: "Mật khẩu tối thiểu 8 ký tự",
              },
            })}
            id="loginPassword"
            className="form-control"
            placeholder="Nhập mật khẩu"
          />
        </div>
        {touchedFields.password && errors.password && (
          <p className="text-danger">{errors.password.message}</p>
        )}
        <div data-mdb-input-init className="form-outline mb-4">
          <label className="form-label" htmlFor="registerRetypePassword">
            Xác nhận mật khẩu
          </label>
          <input
            type="password"
            id="registerRetypePassword"
            className="form-control"
            {...register("retypePassword", {
              required: "Xác nhận mật khẩu không được để trống",
              validate: (value) =>
                value === watch("password") || "Mật khẩu không khớp",
            })}
            placeholder="Nhập lại mật khẩu"
          />
        </div>
        {touchedFields.retypePassword && errors.retypePassword && (
          <p className="text-danger">{errors.retypePassword.message}</p>
        )}
        <div data-mdb-input-init className="form-outline mb-4">
          <label className="form-label" htmlFor="registerPhoneNumber">
            Số điện thoại
          </label>
          <input
            type="text"
            id="registerPhoneNumber"
            {...register("phoneNumber", {
              required: "Số điện thoại không được để trống",
              minLength: {
                value: 3,
                message: "Số điện thoại tối thiểu 3 chữ số",
              },
            })}
            onInput={handleInput}
            className="form-control"
            placeholder="Nhập số điện thoại"
          />
        </div>
        {touchedFields.phoneNumber && errors.phoneNumber && (
          <p className="text-danger">{errors.phoneNumber.message}</p>
        )}
        {/* Submit button */}
        <button
          type="submit"
          style={{ width: "100%" }}
          className="btn btn-primary btn-block mb-4"
        >
          Đăng ký
        </button>
        {/* Register buttons */}
        <div className="text-center">
          <p>
            Đã có tài khoản? <Link to="/login">Đăng nhập ngay</Link>
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
        <SendEmail email={watch("email")} />
      </Dialog>
    </div>
  );
}
