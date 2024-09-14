import React, { useEffect, useState } from "react";
import PitchList from "./comp/PitchList";
import { PitchService } from "../../service/PitchService";
import { useAppDispatch } from "../../store/hooks";
import { useNavigate } from "react-router-dom";
import { showOrHideSpinner } from "../../reducer/SpinnerSlice";
import { FaRegStar, FaStar } from "react-icons/fa";

type SearchModel = {
  keyword: string;
  pitch_type_id: number;
  timer: number;
  pageNumber: number;
  limit: number;
};

export default function Pitch() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const [pitchList, setPitchList] = useState<any>();
  const [search, setSearch] = useState<SearchModel>({
    keyword: "",
    pitch_type_id: 0,
    pageNumber: 1,
    limit: 12,
    timer: 0,
  });

  useEffect(() => {
    dispatch(showOrHideSpinner(true));
    const fetchApi = async () => {
      await PitchService.getInstance()
        .getAll({
          keyword: search.keyword,
          page_number: search.pageNumber,
          limit: search.limit,
        })
        .then((response) => {
          // console.log(response);
          // console.log(search);
          if (response.data.status === 200) {
            setPitchList(response.data.data);
            dispatch(showOrHideSpinner(false));
          }
        })
        .catch((error) => {
          dispatch(showOrHideSpinner(false));
        });
    };
    fetchApi();
  }, [search.timer]);

  const handleInput = (event: any) => {
    let value = event.target.value.replace(/[^0-9]/g, ""); 
    value = value.replace(/^0+(?!$)/, "");
    event.target.value = value;
  };
  return (
    <div>
      <div className="row">
        <div className="col-md-3 col-sm-12">
          <h4 className="mt-3">Bộ lọc tìm kiếm</h4>
          <div className="mt-2 ">
            <label htmlFor="pitch-type">Theo kiểu sân</label>
            <div className="form-check">
              <input
                className="form-check-input"
                type="checkbox"
                defaultValue=""
                id="flexCheckDefault"
              />
              <label className="form-check-label" htmlFor="flexCheckDefault">
                Sân 5
              </label>
            </div>
            <div className="form-check">
              <input
                className="form-check-input"
                type="checkbox"
                defaultValue=""
                id="flexCheckDefault"
              />
              <label className="form-check-label" htmlFor="flexCheckDefault">
                Sân 7
              </label>
            </div>
            <div className="form-check">
              <input
                className="form-check-input"
                type="checkbox"
                defaultValue=""
                id="flexCheckDefault"
              />
              <label className="form-check-label" htmlFor="flexCheckDefault">
                Sân 9
              </label>
            </div>
            <div className="form-check">
              <input
                className="form-check-input"
                type="checkbox"
                defaultValue=""
                id="flexCheckDefault"
              />
              <label className="form-check-label" htmlFor="flexCheckDefault">
                Sân 11
              </label>
            </div>
            <hr className="w-75" />
          </div>
          <div className="mt-2">
            <label htmlFor="price-around">Khoảng giá</label>
            <div className="mt-3">
              <div className="d-flex">
                <input
                  type="text"
                  style={{ maxWidth: "30%" }}
                  min={0}
                  maxLength={7}
                  placeholder="Từ"
                  onChange={handleInput}
                />
                &nbsp;&nbsp;<b>-</b>&nbsp;&nbsp;
                <input
                  type="text"
                  style={{ maxWidth: "30%" }}
                  min={0}
                  maxLength={7}
                  placeholder="Đến"
                  onChange={handleInput}
                />
              </div>
            </div>
            <button className="btn btn-primary w-75 mt-3">Áp dụng</button>
            <hr className="w-75" />
          </div>
          <div className="mt-2">
            <label htmlFor="star">Đánh giá</label>

            {/* 5 sao */}
            <div className="form-check d-flex align-items-center">
              <input
                className="form-check-input"
                type="radio"
                name="rating"
                id="rating5"
                value="5"
                style={{ display: "none" }} // Ẩn radio button
              />
              <label
                className="form-check-label star-label text-warning"
                htmlFor="rating5"
              >
                <FaStar />
                <FaStar />
                <FaStar />
                <FaStar />
                <FaStar />
              </label>
            </div>

            {/* 4 sao trở lên */}
            <div className="form-check d-flex align-items-center">
              <input
                className="form-check-input"
                type="radio"
                name="rating"
                id="rating4"
                value="4"
                style={{ display: "none" }} // Ẩn radio button
              />
              <label
                className="form-check-label star-label text-warning d-flex align-items-center"
                htmlFor="rating4"
              >
                <FaStar />
                <FaStar />
                <FaStar />
                <FaStar />
                <FaRegStar />
                <p className="text-dark m-0 mx-2">trở lên</p>
              </label>
            </div>

            {/* 3 sao trở lên */}
            <div className="form-check d-flex align-items-center">
              <input
                className="form-check-input"
                type="radio"
                name="rating"
                id="rating3"
                value="3"
                style={{ display: "none" }} // Ẩn radio button
              />
              <label
                className="form-check-label star-label text-warning d-flex align-items-center"
                htmlFor="rating3"
              >
                <FaStar />
                <FaStar />
                <FaStar />
                <FaRegStar />
                <FaRegStar />
                <p className="text-dark m-0 mx-2">trở lên</p>
              </label>
            </div>

            {/* 2 sao trở lên */}
            <div className="form-check d-flex align-items-center">
              <input
                className="form-check-input"
                type="radio"
                name="rating"
                id="rating2"
                value="2"
                style={{ display: "none" }} // Ẩn radio button
              />
              <label
                className="form-check-label star-label text-warning d-flex align-items-center"
                htmlFor="rating2"
              >
                <FaStar />
                <FaStar />
                <FaRegStar />
                <FaRegStar />
                <FaRegStar />
                <p className="text-dark m-0 mx-2">trở lên</p>
              </label>
            </div>

            {/* 1 sao trở lên */}
            <div className="form-check d-flex align-items-center">
              <input
                className="form-check-input"
                type="radio"
                name="rating"
                id="rating1"
                value="1"
                style={{ display: "none" }} // Ẩn radio button
              />
              <label
                className="form-check-label star-label text-warning d-flex align-items-center"
                htmlFor="rating1"
              >
                <FaStar />
                <FaRegStar />
                <FaRegStar />
                <FaRegStar />
                <FaRegStar />
                <p className="text-dark m-0 mx-2">trở lên</p>
              </label>
            </div>
            <hr className="w-75" />
          </div>
          <button className="btn btn-secondary w-75 mt-3">Xóa tất cả</button>
        </div>
        <div className="col-md-9 col-sm-12">
          <div className="row">
            <div className="d-flex input-group w-50 m-3">
              <input
                type="text"
                className="form-control"
                placeholder="Tìm kiếm..."
                value={search.keyword}
                onChange={(e) => {
                  setSearch({ ...search, keyword: e.target.value });
                }}
                onKeyDown={(e) => {
                  if (e.key === "Enter") {
                    setSearch({
                      ...search,
                      pageNumber: 1,
                      timer: Date.now(),
                    });
                  }
                }}
              />
              <button
                className="btn btn-primary"
                onClick={() => {
                  setSearch({
                    ...search,
                    timer: Date.now(),
                  });
                }}
              >
                Tìm kiếm
              </button>
            </div>
          </div>
          <div className="row">
            {pitchList?.items && pitchList.items.length > 0 ? (
              <>
                <small className="my-0 mx-3">
                  Số lượng sân: {pitchList.total_items}
                </small>
                <hr className="mx-auto" style={{ width: "95%" }} />
                <PitchList pitchList={pitchList.items} />{" "}
                <div className="col-12 mt-3 d-flex justify-content-center">
                  <nav aria-label="Page navigation">
                    <ul className="pagination ">
                      <li className="page-item">
                        <button
                          className="page-link"
                          onClick={() => {
                            if (search.pageNumber > 1) {
                              setSearch({
                                ...search,
                                pageNumber: search.pageNumber - 1,
                                timer: Date.now(),
                              });
                            }
                          }}
                          aria-label="Previous"
                        >
                          <span aria-hidden="true">«</span>
                        </button>
                      </li>
                      {search.pageNumber === pitchList.total_pages &&
                        pitchList.total_pages > 1 && (
                          <li className="page-item">
                            <button
                              className="page-link"
                              onClick={() => {
                                setSearch({
                                  ...search,
                                  pageNumber: search.pageNumber - 2,
                                  timer: Date.now(),
                                });
                              }}
                            >
                              {search.pageNumber - 2}
                            </button>
                          </li>
                        )}
                      {search.pageNumber > 1 && (
                        <li className="page-item">
                          <button
                            className="page-link"
                            onClick={() => {
                              setSearch({
                                ...search,
                                pageNumber: search.pageNumber - 1,
                                timer: Date.now(),
                              });
                            }}
                          >
                            {search.pageNumber - 1}
                          </button>
                        </li>
                      )}
                      <li className="page-item">
                        <button className="page-link active">
                          {search.pageNumber}
                        </button>
                      </li>
                      {search.pageNumber < pitchList.total_pages && (
                        <li className="page-item">
                          <button
                            className="page-link"
                            onClick={() => {
                              setSearch({
                                ...search,
                                pageNumber: search.pageNumber + 1,
                                timer: Date.now(),
                              });
                            }}
                          >
                            {search.pageNumber + 1}
                          </button>
                        </li>
                      )}
                      {search.pageNumber === 1 && pitchList.total_pages > 2 && (
                        <li className="page-item">
                          <button
                            className="page-link"
                            onClick={() => {
                              setSearch({
                                ...search,
                                pageNumber: search.pageNumber + 2,
                                timer: Date.now(),
                              });
                            }}
                          >
                            {search.pageNumber + 2}
                          </button>
                        </li>
                      )}
                      <li className="page-item">
                        <button
                          className="page-link"
                          onClick={() => {
                            if (search.pageNumber < pitchList.total_pages) {
                              setSearch({
                                ...search,
                                pageNumber: search.pageNumber + 1,
                                timer: Date.now(),
                              });
                            }
                          }}
                          aria-label="Next"
                        >
                          <span aria-hidden="true">»</span>
                        </button>
                      </li>
                    </ul>
                  </nav>
                  <select
                    className="form-select mx-3"
                    style={{ width: "100px", height: "40px" }}
                    aria-label="Chọn giới hạn"
                    value={search.limit} // Dùng value để kiểm soát giá trị được chọn
                    onChange={(e) => {
                      setSearch({
                        ...search,
                        pageNumber: 1,
                        limit: Number(e.target.value),
                        timer: Date.now(),
                      });
                    }}
                  >
                    <option value="9">9</option>
                    <option value="12">12</option>
                    <option value="15">15</option>
                    <option value="18">18</option>
                  </select>
                </div>
              </>
            ) : (
              <div>Không có sân bóng nào</div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
