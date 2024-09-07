import path from "path";
import Layout from "../comp/layout/Layout";
import Home from "../page/Home";

export const RoutersHook: any = {
    path: "/",
    element: <Layout />,
    children: [
        {path : "/home", element: <Home/>}
    ],
  };