import React, { Suspense } from 'react';
import './App.css';
import { Navigate, useRoutes } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import Forbidden from './app/page/errors/Forbidden';
import ErrNetWork from './app/page/errors/ErrNetWork';
import { RoutersHook } from './app/router/routers';
import NotFound from './app/page/errors/NotFound';


import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import './assets/css/spinner.css';
import "react-toastify/dist/ReactToastify.css";
import 'primereact/resources/primereact.css';
import 'primereact/resources/themes/lara-light-cyan/theme.css';
import './App.css';

export const spinner = (
  <div className="progress-spinner text-center ">
    <div className="spinner-border text-primary"></div>
  </div>
);

function App() {
  let router = useRoutes([
    { path: 'not-permission',element: <Forbidden/> }, //403
    { path: '/', element: <Navigate to="/home" replace /> },
    RoutersHook,
    { path: 'err-network',  element: <ErrNetWork/> }, //500
    { path: '*',  element: <NotFound/> }, //404
  ]);

  return (
    <div>
      <ToastContainer></ToastContainer>
      <Suspense fallback={spinner}>{router}</Suspense>
    </div>
  );
}

export default App;
