import React, { useEffect } from "react";
import { UserService } from "../../service/UserService";
import { useNavigate, useParams } from "react-router-dom";
import { useAppDispatch } from "../../store/hooks";
import { toast } from "react-toastify";
import { showOrHideSpinner } from "../../reducer/SpinnerSlice";

export default function ConfirmEmail() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const { token } = useParams<{ token: string }>();

  useEffect(() => {
    const confirmEmail = async () => {
      await UserService.getInstance()
        .confirmEmail(token)
        .then((response) => {
          toast.success(response.data.message, {
            position: "top-right",
            autoClose: 1500,
          });
          dispatch(showOrHideSpinner(false));
        })
        .catch((error) => {
          toast.error(error.response.data.message, {
            position: "top-right",
            autoClose: 1500,
          });
          dispatch(showOrHideSpinner(false));
        });
    };

    if (token) {
      confirmEmail();
      navigate("/login");
    }
  }, [token]);

  return (
    <div className="d-flex justify-content-center align-items-center p-5 bg-light">
      <div className="card shadow-sm p-4" style={{ width: "400px" }}>
        <div className="card-body">
          <h3 className="text-center mb-4">Xác nhận Email</h3>
          <p className="text-center">
            Đang xác nhận email. Vui lòng chờ trong
            giây lát...
          </p>
          <div className="d-flex justify-content-center">
            <div className="spinner-border text-primary" role="status">
              <span className="visually-hidden">Đang tải...</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
