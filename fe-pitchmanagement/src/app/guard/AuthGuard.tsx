import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { showOrHideSpinner } from "../reducer/SpinnerSlice";

export default function AuthGuard(props: any) {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const isAuthenticated = useSelector(
    (state: any) => state.user.isAuthenticated
  );
  const isLoading = useSelector((state: any) => state.user.isLoading);

  useEffect(() => {
    if (isLoading) {
      dispatch(showOrHideSpinner(true));
    } else {
      dispatch(showOrHideSpinner(false));
      if (!isAuthenticated) {
        navigate("/login");
      }
    }
  }, [isLoading, isAuthenticated, navigate, dispatch]);

  if (isLoading) {
    return null;
  }

  if (!isAuthenticated) {
    return null;
  }

  return <>{props.children}</>;
}
