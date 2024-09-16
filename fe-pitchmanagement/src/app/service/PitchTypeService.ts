import axios from "axios";
import axiosCustom from "../config/interceptor/interceptors";
import { ApiUrlUtil } from "../util/ApiUrlUtil";
import { HeadersUtil } from "../util/HeaderUtil";
import { ParamUtil } from "../util/ParamUtil";

export class PitchTypesService {
  private static _pitchTypesService: PitchTypesService;

  public static getInstance(): PitchTypesService {
    if (!PitchTypesService._pitchTypesService) {
        PitchTypesService._pitchTypesService = new PitchTypesService();
    }
    return PitchTypesService._pitchTypesService;
  }


}
