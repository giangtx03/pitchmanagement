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

export type UserResponse = {
  id: number;
  email: string;
  phone_number: string;
  fullname: string;
  address: string;
  avatar: string;
  create_at: string;
  update_at: string;
  role: string;
};

export type DecodedToken = {
  user_id: number;
  sub: string;
  exp: number;
  iat: number;
};

export type UpdateUserDetailsRequest = {
  id: number;
  phone_number: string;
  fullname: string;
  address: string;
  avatar: File | null;
};

export type ChangePasswordRequest = {
  oldPassword: string;
  newPassword: string;
  retypePassword: string;
};
