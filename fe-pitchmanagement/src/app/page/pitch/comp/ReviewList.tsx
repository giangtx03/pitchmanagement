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
import { toast } from "react-toastify";
import { Dialog } from "primereact/dialog";

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
  const [search, setSearch] = useState({
    star_range: 0,
    user_id: 0,
    timer: 0,
    limit: 3,
  });
  const [visible, setVisible] = useState(false);
  const [selectReview, setSelectReview] = useState<any>({
    id: 0,
    star: 0,
    comment: "",
  });

  useEffect(() => {
    const fetchApi = async () => {
      await ReviewService.getInstance()
        .getByPitchId(pitchId, { star: search.star_range, limit: search.limit })
        .then((response) => {
          // console.log(response);
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
  }, [pitchId, search.timer]);

  const onSubmitCreateReview = async () => {
    dispatch(showOrHideSpinner(true));
    await ReviewService.getInstance()
      .createReview({
        pitch_id: pitchId,
        user_id: user.userDetail.id,
        comment: data.comment,
        star: data.star,
      })
      .then((response) => {
        if (response.data.status === 201) {
          toast.success(response.data.message, {
            position: "top-right",
            autoClose: 1500,
          });
          dispatch(showOrHideSpinner(false));
          setSearch({ ...search, star_range: 0, timer: 0, limit: 3 });
          setData({ star: 0, comment: "" });
        }
      })
      .catch((error) => {
        toast.error(error.response.data.message, {
          position: "top-right",
          autoClose: 1500,
        });
        dispatch(showOrHideSpinner(false));
      });
  };

  const onSubmitUpdate = async () => {
    dispatch(showOrHideSpinner(true));
    await ReviewService.getInstance()
      .updateReview({
        id: selectReview.id,
        user_id: user.userDetail.id,
        comment: selectReview.comment,
        star: selectReview.star,
      })
      .then((response) => {
        if (response.data.status === 200) {
          toast.success(response.data.message, {
            position: "top-right",
            autoClose: 1500,
          });
          dispatch(showOrHideSpinner(false));
          setVisible(false);
          setSearch({ ...search, star_range: 0, timer: Date.now(), limit: 3 });
        }
      })
      .catch((error) => {
        toast.error(error.response.data.message, {
          position: "top-right",
          autoClose: 1500,
        });
        dispatch(showOrHideSpinner(false));
      });
  };

  const onDelete = async () => {
    dispatch(showOrHideSpinner(true));
    await ReviewService.getInstance()
      .deleteReview(selectReview.id)
      .then((response) => {
        if (response.data.status === 200) {
          toast.success(response.data.message, {
            position: "top-right",
            autoClose: 1500,
          });
          dispatch(showOrHideSpinner(false));
          setVisible(false);
          setSearch({ ...search, star_range: 0, timer: Date.now(), limit: 3 });
        }
      })
      .catch((error) => {
        toast.error(error.response.data.message, {
          position: "top-right",
          autoClose: 1500,
        });
        dispatch(showOrHideSpinner(false));
      });
  };

  const items = [
    {
      label: "Tùy chọn",
      items: [
        {
          label: "Chỉnh sửa",
          command: () => {
            setVisible(true);
          },
        },
        {
          label: "Xóa",
          command: () => {
            onDelete();
          },
        },
      ],
    },
  ];
  return (
    <div className="col-10 m-auto pt-3">
      <h4>Đánh giá </h4>
      <div className="d-flex mb-2">
        <button
          onClick={() =>
            setSearch({ ...search, star_range: 0, timer: Date.now() })
          }
          className={
            "m-2 text-dark btn btn-outline-info " +
            `${search.star_range == 0 ? "active" : ""}`
          }
        >
          Tất cả
        </button>
        <button
          onClick={() =>
            setSearch({ ...search, star_range: 5, timer: Date.now() })
          }
          className={
            "m-2 text-dark btn btn-outline-info " +
            `${search.star_range == 5 ? "active" : ""}`
          }
        >
          5 sao
        </button>
        <button
          onClick={() =>
            setSearch({ ...search, star_range: 4, timer: Date.now() })
          }
          className={
            "m-2 text-dark btn btn-outline-info " +
            `${search.star_range == 4 ? "active" : ""}`
          }
        >
          4 sao
        </button>
        <button
          onClick={() =>
            setSearch({ ...search, star_range: 3, timer: Date.now() })
          }
          className={
            "m-2 text-dark btn btn-outline-info " +
            `${search.star_range == 3 ? "active" : ""}`
          }
        >
          3 sao
        </button>
        <button
          onClick={() =>
            setSearch({ ...search, star_range: 2, timer: Date.now() })
          }
          className={
            "m-2 text-dark btn btn-outline-info " +
            `${search.star_range == 2 ? "active" : ""}`
          }
        >
          2 sao
        </button>
        <button
          onClick={() =>
            setSearch({ ...search, star_range: 1, timer: Date.now() })
          }
          className={
            "m-2 text-dark btn btn-outline-info " +
            `${search.star_range == 1 ? "active" : ""}`
          }
        >
          1 sao
        </button>
      </div>
      {user.isAuthenticated && (
        <div className="col-md-8 mb-2 col-sm-12 m-auto border-1 shadow p-4">
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
                      onClick={(e) => {
                        menu.current?.toggle(e);
                        setSelectReview(review);
                      }}
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
          {reviews.total_items >= search.limit && reviews.total_pages > 1 && (
            <div className="d-flex mb-2 justify-content-center">
              <button
                className="btn text-dark btn-link text-decoration-none"
                onClick={() => {
                  setSearch({
                    ...search,
                    limit: search.limit + 3,
                    timer: Date.now(),
                  });
                }}
              >
                Xem thêm
              </button>
            </div>
          )}
        </div>
      ) : (
        <p>Chưa có đánh giá nào</p>
      )}
      <Dialog
        header="Chỉnh sửa"
        visible={visible}
        style={{ width: "50vw" }}
        onHide={() => {
          if (!visible) return;
          setVisible(false);
        }}
        dismissableMask={true}
      >
        <div className="col-md-8 mb-2 col-sm-12 m-auto border-1 shadow p-4">
          <div>
            <Rating
              value={selectReview.star}
              onChange={(e) =>
                setSelectReview({ ...selectReview, star: e.target.value })
              }
              cancel={false}
            />
          </div>
          <input
            type="text"
            className="form-control my-3"
            placeholder="Bình luận..."
            value={selectReview?.comment}
            onChange={(e) =>
              setSelectReview({ ...selectReview, comment: e.target.value })
            }
          />
          <div className="mt-auto text-end">
            <button className="btn btn-primary" onClick={onSubmitUpdate}>
              Cập nhật
            </button>
          </div>
        </div>
      </Dialog>
    </div>
  );
}
