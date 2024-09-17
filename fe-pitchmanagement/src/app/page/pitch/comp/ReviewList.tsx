import React, { useEffect, useRef, useState } from "react";
import { ReviewService } from "../../../service/ReviewService";
import { useAppDispatch } from "../../../store/hooks";
import { showOrHideSpinner } from "../../../reducer/SpinnerSlice";
import { Rating } from "primereact/rating";
import { useSelector } from "react-redux";
import { Image } from "primereact/image";
import { ReviewResponse } from "../../../model/Review";
import defaultAvatar from "../../../../assets/images/defaultAvatar.jpg";
import { formatDate } from "../../../util/FormatDate";
import { Menu } from "primereact/menu";
import { BsThreeDotsVertical } from "react-icons/bs";

export default function ReviewList(props: any) {
  const dispatch = useAppDispatch();
  const user = useSelector((state: any) => state.user);
  const { pitchId } = props;

  const [reviews, setReviews] = useState<any>();
  const [data, setData] = useState<any>({
    star: 0,
    comment: "",
  });
  const menu = useRef<Menu>(null);

  useEffect(() => {
    const fetchApi = async () => {
      await ReviewService.getInstance()
        .getByPitchId(pitchId, { request_query: true })
        .then((response) => {
          console.log(response);
          if (response.data.status === 200) {
            setReviews(response.data.data);
            dispatch(showOrHideSpinner(false));
          }
        })
        .catch(() => {
          dispatch(showOrHideSpinner(false));
        });
    };
    dispatch(showOrHideSpinner(true));
    fetchApi();
  }, [pitchId]);

  const onSubmitCreateReview = async () => {
    console.log(data);
  };

  const items = [
    {
      label: "Tùy chọn",
      items: [
        {
          label: "Chỉnh sửa",
        },
        {
          label: "Xóa",
        },
      ],
    },
  ];
  return (
    <div className="col-10 m-auto pt-3">
      <h4>Đánh giá </h4>
      {user.isAuthenticated && (
        <div className="col-md-8 col-sm-12 m-auto border-1 shadow p-4">
          <div>
            <Rating
              value={data.star}
              onChange={(e) => setData({ ...data, star: e.target.value })}
              cancel={false}
            />
          </div>
          <input
            type="text"
            className="form-control my-3"
            placeholder="Bình luận..."
            value={data.comment}
            onChange={(e) => setData({ ...data, comment: e.target.value })}
          />
          <div className="mt-auto text-end">
            <button className="btn btn-primary" onClick={onSubmitCreateReview}>
              Đánh giá
            </button>
          </div>
        </div>
      )}
      {reviews && reviews.items.length > 0 ? (
        <div>
          <p className="text-secondary">
            Số lượng đánh giá: {reviews.total_items}
          </p>
          {reviews.items.map((review: ReviewResponse) => (
            <div key={review.id}>
              <div>
                <div className="d-flex align-items-center">
                  <Image
                    src={
                      process.env.REACT_APP_API_URL +
                      "/images/" +
                      review.user.avatar
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
                  <b className="ms-2">{review.user.fullname}</b>
                </div>
                {user.userDetail.id == review.user.id && (
                  <div className="m-auto text-end">
                    <button
                      className="border-0 btn m-0 p-0 px-2"
                      onClick={(e) => menu.current?.toggle(e)}
                    >
                      <BsThreeDotsVertical />
                    </button>
                    <Menu
                      model={items}
                      popup
                      ref={menu}
                      style={{ width: "14rem" }}
                      id="popup_menu"
                    />
                  </div>
                )}
              </div>
              <div className="ms-5">
                <Rating value={review.star} cancel={false} disabled />
                <p>{formatDate(review.update_at)}</p>
                <p>{review.comment}</p>
              </div>
              <hr />
            </div>
          ))}
        </div>
      ) : (
        <p>Chưa có đánh giá nào</p>
      )}
    </div>
  );
}
