import axios from "axios";
import axiosCustom from "../config/interceptor/interceptors";
import { ApiUrlUtil } from "../util/ApiUrlUtil";
import { HeadersUtil } from "../util/HeaderUtil";
import { ParamUtil } from "../util/ParamUtil";

export class ReviewService {
  private static _reviewService: ReviewService;

  public static getInstance(): ReviewService {
    if (!ReviewService._reviewService) {
        ReviewService._reviewService = new ReviewService();
    }
    return ReviewService._reviewService;
  }

  public getByPitchId(pitch_id : any, searchModel : any){
    const params = ParamUtil.toRequestParams(searchModel);
    const url = ApiUrlUtil.buildQueryString(process.env.REACT_APP_API_URL +  `/reviews/${pitch_id}`, params);
    const headers = HeadersUtil.getHeaders();
    return axios.get(url, {headers});
  }

  public createReview(requestModel : any){
    const url = ApiUrlUtil.buildQueryString(`/reviews`);
    return axiosCustom.post(url, requestModel);
  }

  public updateReview(requestModel : any){
    const url = ApiUrlUtil.buildQueryString(`/reviews`);
    return axiosCustom.put(url, requestModel);
  }

  public deleteReview(id : any){
    const url = ApiUrlUtil.buildQueryString(`/reviews/${id}`);
    return axiosCustom.delete(url);
  }
}
