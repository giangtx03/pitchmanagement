import axios from "axios";
import axiosCustom from "../config/interceptor/interceptors";
import { ApiUrlUtil } from "../util/ApiUrlUtil";
import { HeadersUtil } from "../util/HeaderUtil";

export class PitchService {
  private static _pitchService: PitchService;

  public static getInstance(): PitchService {
    if (!PitchService._pitchService) {
        PitchService._pitchService = new PitchService();
    }
    return PitchService._pitchService;
  }

  public getAll(){
    const url = ApiUrlUtil.buildQueryString(process.env.REACT_APP_API_URL +  `/pitches`);
    const headers = HeadersUtil.getHeaders();
    return axios.get(url, {headers});
  }

}
