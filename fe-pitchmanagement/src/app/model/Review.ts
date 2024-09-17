import { UserResponse } from "./User";

export type ReviewResponse = {
    id : number;
    pitch_id : number;
    comment: string;
    star: number;
    create_at : string;
    update_at : string;
    user : UserResponse;
}