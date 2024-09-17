import { UserResponse } from "./User";

export type PitchResponse = {
  id: number;
  name: string;
  location: string;
  manager: UserResponse;
  images: string[];
  create_at: string;
  update_at: string;
  is_active: boolean;
  avg_star: number;
  sub_pitches : SubPitchResponse[];
};

export type SubPitchResponse = {
  id: number;
  name: string;
  create_at: string;
  update_at: string;
  is_active: boolean;
  pitch_type: string;
  pitch_times: PitchTimeResponse[];
};

export type PitchTimeResponse = {
  price: number;
  start_time: string;
  end_time: string;
  is_active: boolean;
  schedules: string[];
};
