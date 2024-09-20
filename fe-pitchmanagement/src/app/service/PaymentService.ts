import axios from "axios";
import axiosCustom from "../config/interceptor/interceptors";
import { ApiUrlUtil } from "../util/ApiUrlUtil";
import { HeadersUtil } from "../util/HeaderUtil";
import { ParamUtil } from "../util/ParamUtil";

export class PaymentService {
  private static _paymentService: PaymentService;

  public static getInstance(): PaymentService {
    if (!PaymentService._paymentService) {
        PaymentService._paymentService = new PaymentService();
    }
    return PaymentService._paymentService;
  }

  public payBooking(payModel: any) {
    console.log(payModel);
    const url = ApiUrlUtil.buildQueryString(`/payments/create-payment`);
    return axiosCustom.post(url, payModel);
  }

  public confirmPayment(payModel: any) {
    console.log(payModel);
    const params = ParamUtil.toRequestParams(payModel);
    const url = ApiUrlUtil.buildQueryString(`/payments/create-payment`, params);
    return axiosCustom.post(url, payModel);
  }
}
