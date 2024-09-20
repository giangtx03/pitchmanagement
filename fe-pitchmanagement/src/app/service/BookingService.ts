import axios from "axios";
import axiosCustom from "../config/interceptor/interceptors";
import { ApiUrlUtil } from "../util/ApiUrlUtil";
import { HeadersUtil } from "../util/HeaderUtil";
import { ParamUtil } from "../util/ParamUtil";

export class BookingService {
  private static _bookingService: BookingService;

  public static getInstance(): BookingService {
    if (!BookingService._bookingService) {
        BookingService._bookingService = new BookingService();
    }
    return BookingService._bookingService;
  }

  public createBooking(ModelRequest: any ){
    const url = ApiUrlUtil.buildQueryString('/bookings');
    return axiosCustom.post(url, ModelRequest);
  }

  public getBookingById(id: number ){
    const url = ApiUrlUtil.buildQueryString(`/bookings/${id}`);
    return axiosCustom.get(url);
  }

  public getBookingByUserId(id: number, searchModel : any ){
    const params = ParamUtil.toRequestParams(searchModel);
    const url = ApiUrlUtil.buildQueryString(`/bookings/user/${id}`, params);
    return axiosCustom.get(url);
  }

  public getBookingByManagerId(id: number ){
    const url = ApiUrlUtil.buildQueryString(`/bookings/manager/${id}`);
    return axiosCustom.get(url);
  }
}
