import React, { useEffect, useState } from "react";
import PitchList from "./comp/PitchList";
import { PitchService } from "../../service/PitchService";
import { useAppDispatch } from "../../store/hooks";
import { useNavigate } from "react-router-dom";
import { showOrHideSpinner } from "../../reducer/SpinnerSlice";

export default function Pitch() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const [pitchList, setPitchList] = useState();

  useEffect(() => {
    dispatch(showOrHideSpinner(true));
    const fetchApi = async () => {
      await PitchService.getInstance()
        .getAll()
        .then((response) => {
          // console.log(response)
          if (response.data.status === 200) {
            setPitchList(response.data.data.items);
            dispatch(showOrHideSpinner(false));
          }
        })
        .catch((error) => {
          dispatch(showOrHideSpinner(false));
        });
    };
    fetchApi();
  }, []);

  return (
    <div>
      <div className="row">
        <div className="col-md-3 col-sm-12">
          <div className="d-flex input-group">
            <input type="text" className="form-control" placeholder="Tìm kiếm ..." />
            <button className="btn btn-primary">Tìm kiếm</button>
          </div>
        </div>
        <div className="col-md-9 col-sm-12">
          <PitchList pitchList={pitchList} />
        </div>
      </div>
    </div>
  );
}
