import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { showOrHindSpinner } from "../reducer/SpinnerSlice";

export default function AuthGuard(props: any) {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const isAuthenticated = useSelector(
    (state: any) => state.user.isAuthenticated
  );
  const isLoading = useSelector((state: any) => state.user.isLoading);

  useEffect(() => {
    if (isLoading) {
      dispatch(showOrHindSpinner(true));
      if (!isAuthenticated) {
        dispatch(showOrHindSpinner(false));
        navigate("/login");
        return;
      }
    } else {
      dispatch(showOrHindSpinner(false));
      if (!isAuthenticated) {
        navigate("/login");
      }
    }
  }, [isAuthenticated, isLoading, navigate, dispatch]);

  if (isLoading) {
    dispatch(showOrHindSpinner(true));
    return null;
  }

  if (!isAuthenticated) {
    dispatch(showOrHindSpinner(false));
    return null;
  }

  return <>{props.children}</>;
}