import React from 'react'
import Pitch from '../page/pitch/Pitch';
import PitchDetail from '../page/pitch/PitchDetail';

export const PitchRouter: any = {
    path: "pitches",
    children: [
      { path: "", element: <Pitch/> },
      { path: ":id", element: <PitchDetail/> },
    ],
  };