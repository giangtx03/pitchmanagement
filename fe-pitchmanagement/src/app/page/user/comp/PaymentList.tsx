import React, { useEffect } from 'react'
import { useLocation } from 'react-router-dom';
import { toast } from 'react-toastify';

export default function PaymentList() {

  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const message = queryParams.get("message");

  useEffect(() => {
    if(message){
      toast.success(message, {
        position: "top-right",
        autoClose: 1500,
      });
    }
  },[message])
  return (
    <div>PaymentList</div>
  )
}
