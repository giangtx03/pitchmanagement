import React, { useEffect } from "react";
import { useAppDispatch } from "../../../store/hooks";
import { useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import { ChangePasswordRequest } from "../../../model/User";
import Swal from "sweetalert2";
import { showOrHideSpinner } from "../../../reducer/SpinnerSlice";
import { logout } from "../../../reducer/UserSlice";
import { toast } from "react-toastify";
import { UserService } from "../../../service/UserService";
import { useSelector } from "react-redux";

export default function ChangePassword() {
  const dispatch = useAppDispatch();
  const userId = useSelector((state: any) => state.user.userDetail.id);

  const {
    register,
    handleSubmit,
    watch,
    setValue,
    setError,
    clearErrors,
    formState: { errors, touchedFields },
  } = useForm<ChangePasswordRequest>({ mode: "onTouched" });

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

  const onSubmit = (data: any) => {
    Swal.fire({
      title: "Bạn có chắc chắn ?",
      text: "Bạn sẽ không thể hoàn tác hành động này!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Đồng ý",
      cancelButtonText: "Hủy",
      reverseButtons: true,
    }).then(async (result) => {
      if (result.isConfirmed) {
        dispatch(showOrHideSpinner(true));
        await UserService.getInstance()
          .changePassword({
            user_id: userId,
            old_password: data.oldPassword,
            new_password: data.newPassword,
          })
          .then((response: any) => {
            // console.log(response.data);
            if (response.data.status === 200) {
              toast.success(response.data.message, {
                position: "top-right",
                autoClose: 1500,
              });
              dispatch(logout());
              dispatch(showOrHideSpinner(false));
            }
          })
          .catch((error: any) => {
            toast.error(error.response.data.message, {
              position: "top-right",
              autoClose: 1500,
            });
            dispatch(showOrHideSpinner(false));
          });
      }
    });
  };

  return (
    <div className="shadow p-5">
      <div className="row mb-3">
        <h4>Đổi mật khẩu</h4>
      </div>
      <form onSubmit={handleSubmit(onSubmit)} className="d-flex flex-column">
        <div
          data-mdb-input-init
          className="form-outline mb-2 d-flex justify-content-center align-items-center"
        >
          <div className="col-3">
            <label className="form-label" htmlFor="registerPassword">
              Mật khẩu hiện tại
            </label>
          </div>

          <div className="col-9">
            <input
              type="password"
              id="oldPassword"
              {...register("oldPassword", {
                required: "Mật khẩu hiện tại không được để trống",
              })}
              placeholder="Nhập mật khẩu hiện tại"
              className="form-control w-75"
            />
            {touchedFields.oldPassword && errors.oldPassword && (
              <p className="text-danger">{errors.oldPassword.message}</p>
            )}
          </div>
        </div>

        <div
          data-mdb-input-init
          className="form-outline mb-2 d-flex justify-content-center align-items-center"
        >
          <div className="col-3">
            <label className="form-label" htmlFor="registerPassword">
              Mật khẩu mới
            </label>
          </div>

          <div className="col-9">
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
              className="form-control w-75"
            />
            {touchedFields.newPassword && errors.newPassword && (
              <p className="text-danger">{errors.newPassword.message}</p>
            )}
          </div>
        </div>

        <div
          data-mdb-input-init
          className="form-outline mb-2 d-flex justify-content-center align-items-center"
        >
          <div className="col-3">
            <label className="form-label" htmlFor="registerRetypePassword">
              Xác nhận mật khẩu
            </label>
          </div>

          <div className="col-9">
            <input
              type="password"
              id="registerRetypePassword"
              className="form-control w-75"
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
        </div>

        <div className="d-flex justify-content-end mt-4">
          <button
            className="btn btn-success"
            style={{ minWidth: "150px", fontSize: "16px" }}
            type="submit"
          >
            Cập nhật
          </button>
        </div>
      </form>
    </div>
  );
}
