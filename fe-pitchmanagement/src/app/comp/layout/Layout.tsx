import React from "react";
import { Outlet } from "react-router-dom";
import Header from "./Header";
import Footer from "./Footer";
import { useAppSelector } from "../../store/hooks";
import { spinner } from "../../../App";

export default function Layout() {
  const loading = useAppSelector((state) => state.spinner.loading);

  return (
    <>
      {loading && spinner}
      <div className="container">
        <div className="row">
          <Header />
        </div>
        <div className="row">
          <Outlet />
        </div>
        <div className="row">
          <Footer />
        </div>
      </div>
    </>
  );
}
