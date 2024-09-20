import React from "react";
import { Navigate } from "react-router-dom";
import AuthGuard from "../guard/AuthGuard";
import UserProfile from "../page/user/comp/UserProfile";
import User from "../page/user/User";
import ChangePassword from "../page/user/comp/ChangePassword";
import BookingList from "../page/user/comp/BookingList";
import PaymentList from "../page/user/comp/PaymentList";

export const UserRouter: any = {
  path: "users",
  element: (
    <AuthGuard>
      <User />
    </AuthGuard>
  ),
  children: [
    { path: "", element: <Navigate to="profile" /> },
    { path: "profile", element: <UserProfile /> },
    { path: "change-password", element: <ChangePassword /> },
    { path: "bookings", element: <BookingList /> },
    { path: "payments", element: <PaymentList /> },
  ],
};
