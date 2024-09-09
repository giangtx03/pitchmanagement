import React from "react";
import { useAppDispatch } from "../../store/hooks";
import { showOrHideSpinner } from "../../reducer/SpinnerSlice";
import { UserService } from "../../service/UserService";
import { toast } from "react-toastify";

export default function SendEmail(props: any) {
  const { email } = props;
  const dispatch = useAppDispatch();

  const handleResendEmail = async () => {
    dispatch(showOrHideSpinner(true));
    await UserService.getInstance()
      .sendEmail(email)
      .then((response) => {
        if (response.data.status === 200) {
          dispatch(showOrHideSpinner(false));
          toast.success(response.data.message, {
            position: "top-right",
            autoClose: 1500,
          });
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
    <div className="container text-center bg-body-secondary p-2">
      <img
        src="https://cdn4.iconfinder.com/data/icons/social-media-logos-6/512/112-gmail_email_mail-512.png"
        style={{ width: "200px" }}
        alt="Img email"
      />
      <h3>Xác thực Email</h3>
      <p>
        Chúng tôi đã gửi tin nhắn qua địa chỉ email <b>{email}</b> để xác thực
        tài khoản. Sau khi nhận email vui lòng thực hiện theo chỉ dẫn mà email
        đã cung cấp để hoàn thành đăng ký tài khoản của bạn.
      </p>
      <hr />
      <div className="d-flex justify-content-center align-items-center">
        <p className="mb-0">Nếu bạn không nhận được email</p>
        <button
          type="button"
          onClick={handleResendEmail}
          className="mx-2 text-success btn-link p-0"
          style={{
            border: "none",
            backgroundColor: "transparent",
          }}
        >
          Gửi lại email xác thực
        </button>
      </div>
    </div>
  );
}
