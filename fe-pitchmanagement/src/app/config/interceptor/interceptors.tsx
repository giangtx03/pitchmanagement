import axios from "axios";

// Tạo một instance của Axios
const axiosCustom = axios.create({
  baseURL: process.env.REACT_APP_AUTH_URL, // Thay bằng URL gốc của API của bạn
});

// Thêm một interceptor để tự động thêm token vào tất cả các yêu cầu
axiosCustom.interceptors.request.use(
  (config) => {
    if (config.data instanceof FormData) {
      delete config.headers["Content-Type"];
    } else {
      config.headers["Content-Type"] = "application/json";
    }
    const token = localStorage.getItem("access_token"); // Lấy token từ localStorage
    if (token) {
      config.headers["Authorization"] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Thêm một interceptor để xử lý lỗi từ phía server
axiosCustom.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    // Xử lý các lỗi 401, 403, 500, hoặc làm mới token ở đây nếu cần
    if (error.response.status === 401) {
      // Ví dụ: Chuyển hướng đến trang đăng nhập nếu không được phép
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

export default axiosCustom;