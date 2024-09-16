import axios from "axios";
import axiosCustom from "../config/interceptor/interceptors";
import { ApiUrlUtil } from "../util/ApiUrlUtil";
import { HeadersUtil } from "../util/HeaderUtil";
import { ParamUtil } from "../util/ParamUtil";

export class PitchService {
  private static _pitchService: PitchService;

  public static getInstance(): PitchService {
    if (!PitchService._pitchService) {
        PitchService._pitchService = new PitchService();
    }
    return PitchService._pitchService;
  }

  public getAll(searchModel : any){
    const params = ParamUtil.toRequestParams(searchModel);
    const url = ApiUrlUtil.buildQueryString(process.env.REACT_APP_API_URL +  `/pitches`, params);
    const headers = HeadersUtil.getHeaders();
    return axios.get(url, {headers});
  }

  public getPitchById(id : any, modelRequest : any){
    const params = ParamUtil.toRequestParams(modelRequest);
    const url = ApiUrlUtil.buildQueryString(process.env.REACT_APP_API_URL +  `/pitches/${id}`, params);
    const headers = HeadersUtil.getHeaders();
    return axios.get(url, {headers});
  }
}
