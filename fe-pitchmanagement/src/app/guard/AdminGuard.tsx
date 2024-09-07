import { useDispatch, useSelector } from "react-redux";
import { Navigate, useNavigate } from "react-router-dom";
import { showOrHindSpinner } from "../reducer/SpinnerSlice";
import { useEffect } from "react";

export default function AdminGuard(props: any) {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const isAuthenticated = useSelector(
    (state: any) => state.user.isAuthenticated
  );
  const isLoading = useSelector((state: any) => state.user.isLoading);
  const user = useSelector((state: any) => state.user.userDetail);

  useEffect(() => {
    if (isLoading) {
      dispatch(showOrHindSpinner(true));
    } else {
      dispatch(showOrHindSpinner(false));
      if (!isAuthenticated) {
        navigate("/login");
      } else if (user.role !== "ADMIN") {
        navigate("/not-permission");
      }
    }
  }, [isAuthenticated, isLoading, navigate, dispatch, user?.role]);

  if (!isLoading && (!isAuthenticated || user?.role !== "ADMIN")) {
    dispatch(showOrHindSpinner(false));
    return null;
  }

  return <>{props.children}</>;
}