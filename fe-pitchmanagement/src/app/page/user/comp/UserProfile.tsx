import React, { useEffect, useRef, useState } from "react";
import { useAppDispatch } from "../../../store/hooks";
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import { UpdateUserDetailsRequest, UserDetails } from "../../../model/User";
import { showOrHideSpinner } from "../../../reducer/SpinnerSlice";
import { Image } from "primereact/image";
import defaultAvatar from "../../../../assets/images/defaultAvatar.jpg";
import { FiUpload } from "react-icons/fi";
import { useForm } from "react-hook-form";
import { formatDate } from "../../../util/FormatDate";
import Swal from "sweetalert2";
import { login } from "../../../reducer/UserSlice";
import { toast } from "react-toastify";
import { UserService } from "../../../service/UserService";

export default function UserProfile() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const userDetail = useSelector((state: any) => state.user.userDetail);

  const [preview, setPreview] = useState<string | null>(null);
  const fileInputRef = useRef<HTMLInputElement | null>(null);

  const [user, setUser] = useState<UserDetails>();
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors, touchedFields },
  } = useForm<UpdateUserDetailsRequest>({
    mode: "onTouched",
    defaultValues: {
      fullname: userDetail.fullname,
      id: userDetail.id,
      phone_number: userDetail.phone_number,
      address: userDetail.address,
    },
  });

  useEffect(() => {
    dispatch(showOrHideSpinner(true));
    setTimeout(() => {
      setUser(userDetail);
      dispatch(showOrHideSpinner(false));
    }, 300);
  }, [navigate, userDetail]);

  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      setPreview(URL.createObjectURL(file));
    }
  };

  const handleInput = (event: any) => {
    event.target.value = event.target.value.replace(/[^0-9]/g, "");
  };

  const onSubmit = (data: any) => {
    // console.log(data);
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
        const formData = new FormData();
        formData.append("id", data.id);
        formData.append("fullname", data.fullname);
        formData.append("phoneNumber", data.phone_number);
        formData.append("address", data.address || "");
        if (data.avatar?.[0]) {
          formData.append("avatar", data.avatar[0]);
        }
        await UserService.getInstance()
          .updateUserDetails(formData)
          .then((response) => {
            // console.log(response.data);
            if (response.data.status === 200) {
              toast.success(response.data.message, {
                position: "top-right",
                autoClose: 1500,
              });
              // window.location.reload();
              dispatch(login(response.data.data));
              dispatch(showOrHideSpinner(false));
            }
          })
          .catch((error: any) => {
            toast.error(error.response.data.message, {
              position: "top-right",
              autoClose: 1500,
            });
            // console.log(error);
            dispatch(showOrHideSpinner(false));
          });
      }
    });
  };

  return user ? (
    <div className="shadow p-5">
      <div className="row">
        <h4>Thông tin người dùng</h4>
      </div>
      <form onSubmit={handleSubmit(onSubmit)} className="row">
        <div className="col-9 mt-4">
          <div className="mb-4">
            <div className="card-body">
              <div className="row">
                <div
                  data-mdb-input-init
                  className="form-outline mb-2 d-flex justify-content-center align-items-center"
                >
                  <div className="col-3">
                    <label className="form-label" htmlFor="registerFullname">
                      Email
                    </label>
                  </div>
                  <div className="col-9">
                    <input
                      type="text"
                      id="registerFullname"
                      value={user?.email}
                      className="form-control"
                      disabled
                    />
                  </div>
                </div>
                <div
                  data-mdb-input-init
                  className="form-outline mb-2 d-flex justify-content-center align-items-center"
                >
                  <div className="col-3">
                    <label className="form-label" htmlFor="registerFullname">
                      Họ và tên
                    </label>
                  </div>
                  <div className="col-9">
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
                </div>
                {touchedFields.fullname && errors.fullname && (
                  <p className="text-danger">{errors.fullname.message}</p>
                )}
              </div>
              <div className="row">
                <div
                  data-mdb-input-init
                  className="form-outline mb-2 d-flex justify-content-center align-items-center"
                >
                  <div className="col-3">
                    <label className="form-label" htmlFor="registerPhoneNumber">
                      Số điện thoại
                    </label>
                  </div>
                  <div className="col-9">
                    <input
                      type="text"
                      id="registerPhoneNumber"
                      {...register("phone_number", {
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
                </div>
                {touchedFields.phone_number && errors.phone_number && (
                  <p className="text-danger">{errors.phone_number.message}</p>
                )}
              </div>
              <div className="row">
                <div
                  data-mdb-input-init
                  className="form-outline mb-2 d-flex justify-content-center align-items-center"
                >
                  <div className="col-3">
                    <label className="form-label" htmlFor="registerAddress">
                      Địa chỉ
                    </label>
                  </div>
                  <div className="col-9">
                    <input
                      type="text"
                      id="registerAddress"
                      {...register("address")}
                      className="form-control"
                      placeholder="Nhập địa chỉ"
                    />
                  </div>
                </div>
              </div>
              <div
                data-mdb-input-init
                className="form-outline mb-2 d-flex justify-content-center align-items-center"
              >
                <div className="col-3">
                  <label className="form-label" htmlFor="registerFullname">
                    Ngày tạo
                  </label>
                </div>
                <div className="col-9">
                  <input
                    type="text"
                    id="registerFullname"
                    value={formatDate(userDetail.create_at)}
                    className="form-control"
                    disabled
                  />
                </div>
              </div>
              <div
                data-mdb-input-init
                className="form-outline mb-2 d-flex justify-content-center align-items-center"
              >
                <div className="col-3">
                  <label className="form-label" htmlFor="registerFullname">
                    Ngày chỉnh sửa
                  </label>
                </div>
                <div className="col-9">
                  <input
                    type="text"
                    id="registerFullname"
                    value={formatDate(userDetail.update_at)}
                    className="form-control"
                    disabled
                  />
                </div>
              </div>
              <hr />
            </div>
          </div>
          <div className="d-flex justify-content-end">
            <button
              className="btn btn-success"
              style={{ minWidth: "150px", fontSize: "16px" }}
              type="submit"
            >
              Lưu
            </button>
          </div>
        </div>
        <div className="col-3">
          <div
            className="image-container card-body"
            onClick={() => fileInputRef.current?.click()}
          >
            <Image
              src={
                preview ||
                process.env.REACT_APP_API_URL + `/images/${user?.avatar}`
              }
              alt="User Avatar"
              width="200"
              height="200"
              onError={(e) => {
                const target = e.target as HTMLImageElement;
                target.src = defaultAvatar;
              }}
              style={{
                borderRadius: "50%",
                overflow: "hidden",
                objectFit: "cover",
              }}
            />
            <div className="overlay">
              <div className="icon-wrapper">
                <FiUpload className="upload-icon" />
              </div>
            </div>
            <input
              type="file"
              {...register("avatar")}
              onChange={(e) => {
                register("avatar").onChange(e);
                handleImageChange(e);
              }}
              accept="image/*"
              className="form-control"
              ref={(e) => {
                register("avatar").ref(e);
                fileInputRef.current = e;
              }}
              hidden
            />
          </div>
          <p className="text-dark mb-3">Thay đổi ảnh đại diện</p>
          <p className="text-muted mb-1">Dụng lượng file tối đa 1 MB</p>
          <p className="text-muted mb-1">Định dạng:.JPEG, .PNG</p>
        </div>
      </form>
    </div>
  ) : (
    <></>
  );
}
