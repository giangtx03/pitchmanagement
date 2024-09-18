import { PitchResponse, PitchTimeResponse, SubPitchResponse } from "./Pitch";
import { UserResponse } from "./User";

export type BookingResponse = {
  id: number;
  note: string;
  booking_date: string;
  status: string;
  deposit: number;
  pitch: PitchResponse;
  pitch_time: PitchTimeResponse;
  sub_pitch: SubPitchResponse;
  user: UserResponse;
  create_at: string;
  update_at: string;
};
