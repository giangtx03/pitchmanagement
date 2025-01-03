import axios from "axios";
import axiosCustom from "../config/interceptor/interceptors";
import { ApiUrlUtil } from "../util/ApiUrlUtil";
import { HeadersUtil } from "../util/HeaderUtil";

export class UserService {
  private static _userService: UserService;

  public static getInstance(): UserService {
    if (!UserService._userService) {
      UserService._userService = new UserService();
    }
    return UserService._userService;
  }

  public login(modelLogin: any) {
    const url = ApiUrlUtil.buildQueryString(
      process.env.REACT_APP_API_URL + "/users/login"
    );
    return axiosCustom.post(url, modelLogin);
  }

  public register(modelRegister: any) {
    const url = ApiUrlUtil.buildQueryString(
      process.env.REACT_APP_API_URL + "/users/register"
    );
    return axiosCustom.post(url, modelRegister);
  }

  public getUserDetails(id: number) {
    const url = ApiUrlUtil.buildQueryString(`/users/${id}`);
    return axiosCustom.get(url);
  }

  public updateUserDetails(modelUpdateUserDetails: any) {
    const url = ApiUrlUtil.buildQueryString('/users');
    return axiosCustom.put(url, modelUpdateUserDetails);
  }

  public changePassword(modelChangePassword: any) {
    const url = ApiUrlUtil.buildQueryString('/users/change-password');
    return axiosCustom.put(url, modelChangePassword);
  }

  public sendEmail(email: any) {
    const url = ApiUrlUtil.buildQueryString(process.env.REACT_APP_API_URL +  `/users/resend-confirm-email/${email}`);
    const headers = HeadersUtil.getHeaders();
    return axios.get(url, {headers});
  }

  public confirmEmail(email: any) {
    const url = ApiUrlUtil.buildQueryString(process.env.REACT_APP_API_URL +  `/users/confirm-email/${email}`);
    const headers = HeadersUtil.getHeaders();
    return axios.get(url, {headers});
  }

  public forgotPassword(email: any) {
    const url = ApiUrlUtil.buildQueryString(process.env.REACT_APP_API_URL +  `/users/forgot-password/${email}`);
    const headers = HeadersUtil.getHeaders();
    return axios.get(url, {headers});
  }

  public renewPassword(modelRenewPassword: any) {
    const url = ApiUrlUtil.buildQueryString(process.env.REACT_APP_API_URL +  `/users/renew-password/${modelRenewPassword.token}`);
    const headers = HeadersUtil.getHeaders();
    return axios.post(url, {new_password : modelRenewPassword.new_password} ,{headers});
  }
}
