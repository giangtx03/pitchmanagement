package com.pitchmanagement.services;

import com.pitchmanagement.dtos.requests.sub_pitch.CreateSubPitchRequest;
import com.pitchmanagement.dtos.requests.sub_pitch.UpdateSubPitchRequest;

public interface SubPitchService {

    void createSubPitch(CreateSubPitchRequest createSubPitchRequest) throws Exception;
    void updateSubPitch(UpdateSubPitchRequest updateSubPitchRequest) throws Exception;

}
