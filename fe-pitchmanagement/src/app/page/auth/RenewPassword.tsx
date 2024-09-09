import React, { useEffect } from "react";
import { useForm } from "react-hook-form";
import { useNavigate, useParams } from "react-router-dom";
import { useAppDispatch } from "../../store/hooks";
import { showOrHideSpinner } from "../../reducer/SpinnerSlice";
import { UserService } from "../../service/UserService";
import { toast } from "react-toastify";

export default function RenewPassword() {
  const { token } = useParams<{ token: string }>();

  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    watch,
    setValue,
    setError,
    clearErrors,
    formState: { errors, touchedFields },
  } = useForm<{ newPassword: string; retypePassword: string }>({
    mode: "onTouched",
  });

  useEffect(() => {
    const retypePassword = watch("retypePassword");
    if (retypePassword && retypePassword !== watch("newPassword")) {
      setError("retypePassword", {
        type: "manual",
        message: "Mật khẩu không khớp",
      });
    } else {
      clearErrors("retypePassword");
    }
  }, [watch("newPassword"), watch, setError, clearErrors]);

  const onSubmit = async (data: any) => {
    console.log(token);
    dispatch(showOrHideSpinner(true));

    await UserService.getInstance()
      .renewPassword({ token: token, new_password: data.newPassword })
      .then((response) => {
        if (response.data.status === 200) {
          toast.success(response.data.message, {
            position: "top-right",
            autoClose: 1500,
          });
          dispatch(showOrHideSpinner(false));
          navigate("/login")
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
      <h3 className="mb-4">Cập nhật mật khẩu mới</h3>
      <form onSubmit={handleSubmit(onSubmit)} className="w-100">
        <div
          data-mdb-input-init
          className="form-outline mb-2 d-flex flex-column"
        >
          <label className="form-label" htmlFor="registerPassword">
            Mật khẩu mới
          </label>
          <input
            type="password"
            id="registerPassword"
            {...register("newPassword", {
              required: "Mật khẩu mới không được để trống",
              minLength: {
                value: 8,
                message: "Mật khẩu mới tối thiểu 8 ký tự",
              },
            })}
            onChange={(e) => {
              const trimmedValue = e.target.value.trim();
              setValue("newPassword", trimmedValue);
            }}
            placeholder="Nhập mật khẩu mới"
            className="form-control"
          />
          {touchedFields.newPassword && errors.newPassword && (
            <p className="text-danger">{errors.newPassword.message}</p>
          )}
        </div>

        <div
          data-mdb-input-init
          className="form-outline mb-2 d-flex flex-column"
        >
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
                value === watch("newPassword") || "Mật khẩu không khớp",
            })}
            placeholder="Xác nhận mật khẩu"
          />
          {touchedFields.retypePassword && errors.retypePassword && (
            <p className="text-danger">{errors.retypePassword.message}</p>
          )}
        </div>

        {/* Submit button */}
        <button type="submit" className="btn btn-primary btn-block mb-4 w-100">
          Xác nhận
        </button>
      </form>
    </div>
  );
}
