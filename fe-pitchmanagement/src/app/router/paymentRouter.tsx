import React from 'react'
import Pitch from '../page/pitch/Pitch';
import PitchDetail from '../page/pitch/PitchDetail';
import VNPayReturn from '../page/user/comp/VNPayReturn';
import AuthGuard from '../guard/AuthGuard';

export const PaymentRouter: any = {
    path: "payments",
    children: [
      { path: "", element: <Pitch/> },
      { path: "vnpay-return", element: <AuthGuard> <VNPayReturn/> </AuthGuard> },
    ],
  };