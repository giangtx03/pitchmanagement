import React, { useEffect, useState } from "react";
import PitchList from "./comp/PitchList";
import { PitchService } from "../../service/PitchService";
import { useAppDispatch } from "../../store/hooks";
import { useNavigate } from "react-router-dom";
import { showOrHideSpinner } from "../../reducer/SpinnerSlice";
import { FaRegStar, FaStar } from "react-icons/fa";
import { toast } from "react-toastify";

type SearchModel = {
  keyword: string;
  pitch_types: number[];
  start_price: number;
  end_price: number;
  star_range: number;
  timer: number;
  pageNumber: number;
  limit: number;
};

export default function Pitch() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const [pitchList, setPitchList] = useState<any>();
  const [pitchTypes, setPitchTypes] = useState<any>();
  const [search, setSearch] = useState<SearchModel>({
    keyword: "",
    pitch_types: [],
    start_price: 0,
    end_price: 0,
    star_range: 0,
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
          start_price: search.start_price,
          end_price: search.end_price,
          star_range: search.star_range,
          pitch_types: search.pitch_types,
          limit: search.limit,
          request_query: true,
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
          toast.error(error.response.data.message, {
            position: "top-right",
            autoClose: 1500,
          });
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

  const handleRadioChange = (e: any) => {
    setSearch({
      ...search,
      star_range: e.target.value,
      timer: Date.now(),
    });
  };

  const handleCheckboxChange = (e: any) => {
    const value = Number.parseInt(e.target.value);
    const isChecked = e.target.checked;

    if (isChecked) {
      // Thêm giá trị vào mảng pitch_types nếu radio button được chọn
      setSearch({
        ...search,
        pitch_types: [...search.pitch_types, value], // Tạo một mảng mới với giá trị mới
        timer: Date.now(),
      });
    } else {
      // Loại bỏ giá trị khỏi mảng pitch_types nếu radio button bị bỏ chọn
      setSearch({
        ...search,
        pitch_types: search.pitch_types.filter((type) => type !== value), // Lọc phần tử không muốn
        timer: Date.now(),
      });
    }
  };
  return (
    <div>
      <div className="row">
        <div className="col-md-3 col-sm-12">
          <h4 className="mt-3">Bộ lọc tìm kiếm</h4>
          {/* {"Kiểu sân"} */}
          <div className="mt-2 ">
            <label htmlFor="pitch-type">Theo kiểu sân</label>
            <div className="form-check">
              <input
                className="form-check-input cursor-pointer"
                type="checkbox"
                value="1"
                id="flexCheckDefault1"
                checked={search.pitch_types.includes(1)}
                onChange={handleCheckboxChange}
              />
              <label className="form-check-label" htmlFor="flexCheckDefault1">
                Sân 5
              </label>
            </div>
            <div className="form-check">
              <input
                className="form-check-input cursor-pointer"
                type="checkbox"
                value="2"
                id="flexCheckDefault2"
                checked={search.pitch_types.includes(2)}
                onChange={handleCheckboxChange}
              />
              <label className="form-check-label" htmlFor="flexCheckDefault2">
                Sân 7
              </label>
            </div>
            <div className="form-check">
              <input
                className="form-check-input cursor-pointer"
                type="checkbox"
                value="3"
                id="flexCheckDefault3"
                checked={search.pitch_types.includes(3)}
                onChange={handleCheckboxChange}
              />
              <label className="form-check-label" htmlFor="flexCheckDefault3">
                Sân 9
              </label>
            </div>
            <div className="form-check">
              <input
                className="form-check-input cursor-pointer"
                type="checkbox"
                value="4"
                id="flexCheckDefault4"
                checked={search.pitch_types.includes(4)}
                onChange={handleCheckboxChange}
              />
              <label className="form-check-label" htmlFor="flexCheckDefault4">
                Sân 11
              </label>
            </div>
            <hr className="w-75" />
          </div>
          {/* {"khoảng giá"} */}
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
                  onChange={(e) => {
                    handleInput(e);
                    setSearch({
                      ...search,
                      start_price: Number.parseInt(e.target.value),
                    });
                  }}
                />
                &nbsp;&nbsp;<b>-</b>&nbsp;&nbsp;
                <input
                  type="text"
                  style={{ maxWidth: "30%" }}
                  min={0}
                  maxLength={7}
                  placeholder="Đến"
                  onChange={(e) => {
                    handleInput(e);
                    console.log(e.target.value);
                    setSearch({
                      ...search,
                      end_price: Number.parseInt(e.target.value),
                    });
                  }}
                />
              </div>
            </div>
            <button
              className="btn btn-primary w-75 mt-3"
              onClick={() => {
                if (
                  search.start_price <= search.end_price ||
                  (search.start_price === 0 && search.end_price === 0)
                ) {
                  setSearch({ ...search, timer: Date.now() });
                } else {
                  toast.warn("Khoảng giá không phù hợp", {
                    position: "top-left",
                    autoClose: 1500,
                  });
                }
              }}
            >
              Áp dụng
            </button>
            <hr className="w-75" />
          </div>
          {/* {"đánh giá"} */}
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
                onChange={handleRadioChange}
                checked={search.star_range == 5}
              />
              <label
                className="form-check-label star-label text-warning cursor-pointer"
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
                onChange={handleRadioChange}
                checked={search.star_range == 4}
              />
              <label
                className="form-check-label star-label text-warning d-flex align-items-center cursor-pointer"
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
                onChange={handleRadioChange}
                checked={search.star_range == 3}
              />
              <label
                className="form-check-label star-label text-warning cursor-pointer d-flex align-items-center"
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
                onChange={handleRadioChange}
                checked={search.star_range == 2}
              />
              <label
                className="form-check-label star-label text-warning d-flex cursor-pointer align-items-center"
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
                onChange={handleRadioChange}
                checked={search.star_range == 1}
              />
              <label
                className="form-check-label star-label text-warning cursor-pointer d-flex align-items-center"
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
          {/* {"xóa bộ lọc"} */}
          <button
            className="btn btn-secondary w-75 mt-3"
            onClick={() => {
              setSearch({
                keyword: "",
                pitch_types: [],
                start_price: 0,
                end_price: 9999999,
                star_range: 0,
                pageNumber: 1,
                limit: 12,
                timer: Date.now(),
              });
            }}
          >
            Xóa tất cả
          </button>
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
