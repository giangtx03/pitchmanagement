import React from "react";
import { useParams } from "react-router-dom";

export default function PitchDetail() {
  const { id } = useParams();

  return <div>PitchDetail {id}</div>;
}
