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
}
