import { createSlice } from "@reduxjs/toolkit";
import { TokenService } from "../service/TokenService";

const initialState = {
  isAuthenticated: false,
  isLoading: true,
  userDetail: {
    id: 0,
    email: "",
    phone_number: "",
    fullname: "",
    address: "",
    avatar: "",
    create_at: "",
    update_at: "",
    role: "",
  },
};

const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    login: (state, action) => {
      state.isAuthenticated = true;
      state.isLoading = false;
      state.userDetail = action.payload;
    },
    logout: (state) => {
      TokenService.getInstance().removeToken();
      state.isAuthenticated = false;
      state.isLoading = false;
      state.userDetail = {
        id: 0,
        email: "",
        phone_number: "",
        fullname: "",
        address: "",
        avatar: "",
        create_at: "",
        update_at: "",
        role: "",
      };
    },
    setLoading(state) {
      state.isLoading = false;
    }
  },
});

export default userSlice.reducer;
export const { login, logout, setLoading } = userSlice.actions;