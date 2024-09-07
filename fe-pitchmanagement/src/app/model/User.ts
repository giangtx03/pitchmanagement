export type LoginRequest = {
  email: string;
  password: string;
};

export type RegisterRequest = {
  email: string;
  password: string;
  phoneNumber: string;
  fullname: string;
  retypePassword: string;
};
