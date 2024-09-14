import React from "react";
import { PitchResponse } from "../../../model/Pitch";
import defaultAvatar from "../../../../assets/images/defaultAvatar.jpg";
import defaultSanBong from "../../../../assets/images/defaultSanBong.jpeg";
import { Image } from "primereact/image";

export default function PitchList(props: any) {
  const { pitchList } = props;

  return (
    <div>
      <div className="row">
        {/* Hiển thị danh sách pitches ở đây */}
        {pitchList.map((pitch: PitchResponse) => (
          <div key={pitch.id} className="col-xl-4 col-md-6 col-sm-12 mb-3">
            <div className="card" style={{ width: "18rem" }}>
              <img
                src={
                  process.env.REACT_APP_API_URL + "/images/" + pitch.images[0]
                }
                onError={(e) => {
                  const target = e.target as HTMLImageElement;
                  target.src = defaultSanBong;
                }}
                className="card-img-top"
                alt="..."
              />
              <div className="card-body">
                <h5 className="card-title">{pitch.name}</h5>
                <div className="d-flex align-items-center">
                  <Image
                    src={
                      process.env.REACT_APP_API_URL +
                      "/images/" +
                      pitch.manager.avatar
                    }
                    alt="User Avatar"
                    width="30"
                    height="30"
                    onError={(e) => {
                      const target = e.target as HTMLImageElement;
                      target.src = defaultAvatar;
                    }}
                    style={{
                      borderRadius: "50%",
                      overflow: "hidden",
                      objectFit: "cover",
                    }}
                  />
                  <b>{pitch.manager.fullname}</b>
                </div>
                <p className="card-text m-0">
                  {" "}
                  Địa điểm:
                  {" " + pitch.location}
                </p>
                <p className="card-text">
                  Đánh giá :{" "}
                  <b className="text-warning">
                    {pitch.avg_star > 0 ? (
                      <>{pitch.avg_star.toFixed(2)}/5 </>
                    ) : (
                      "chưa có đánh giá"
                    )}
                  </b>
                </p>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
