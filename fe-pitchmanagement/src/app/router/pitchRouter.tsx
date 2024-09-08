import React from 'react'
import Pitch from '../page/pitch/Pitch';

export const PitchRouter: any = {
    path: "pitches",
    children: [
      { path: "", element: <Pitch/> },
    ],
  };