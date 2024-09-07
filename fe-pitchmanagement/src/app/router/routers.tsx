import path from "path";
import Layout from "../comp/layout/Layout";
import Home from "../page/Home";
import Login from "../page/Login";
import Register from "../page/Register";

export const RoutersHook: any = {
    path: "/",
    element: <Layout />,
    children: [
        {path : "/home", element: <Home/>},
        {path : "/login", element:<Login/>},
        {path : "/register", element:<Register/>},
    ],
  };