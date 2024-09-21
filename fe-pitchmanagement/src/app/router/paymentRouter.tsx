import PaymentList from '../page/user/comp/PaymentList';

export const PaymentRouter: any = {
    path: "payments",
    children: [
      { path: "", element: <PaymentList/> },
    ],
  };