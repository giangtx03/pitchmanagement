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
    const url = ApiUrlUtil.buildQueryString(`/payments/create-payment`);
    return axiosCustom.post(url, payModel);
  }

  public getPaymentsByManagerId(id: number, searchModel: any) {
    const params = ParamUtil.toRequestParams(searchModel);
    const url = ApiUrlUtil.buildQueryString(`/payments/manager/${id}`, params);
    return axiosCustom.get(url);
  }
}
