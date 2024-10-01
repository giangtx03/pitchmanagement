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
      <div className="row" style={{ marginBottom: "100px" }}>
        <Header />
      </div>
      <div className="container">
        <div className="row my-3">
          <Outlet />
        </div>
      </div>
      <div className="row">
        <Footer />
      </div>
    </>
  );
}
