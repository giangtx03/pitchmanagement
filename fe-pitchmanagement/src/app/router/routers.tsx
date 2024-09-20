import Layout from "../comp/layout/Layout";
import ConfirmEmail from "../page/auth/ConfirmEmail";
import ForgotPassword from "../page/auth/ForgotPassword";
import RenewPassword from "../page/auth/RenewPassword";
import Home from "../page/Home";
import Login from "../page/Login";
import Register from "../page/Register";
import { PaymentRouter } from "./paymentRouter";
import { PitchRouter } from "./pitchRouter";
import { UserRouter } from "./userRouter";

export const RoutersHook: any = {
  path: "/",
  element: <Layout />,
  children: [
    { path: "home", element: <Home /> },
    { path: "login", element: <Login /> },
    { path: "register", element: <Register /> },
    { path: "confirm-email/:token", element: <ConfirmEmail /> },
    { path: "renew-password/:token", element: <RenewPassword /> },
    { path: "forgot-password", element: <ForgotPassword /> },
    {...UserRouter},
    {...PitchRouter},
    {...PaymentRouter},
  ],
};
