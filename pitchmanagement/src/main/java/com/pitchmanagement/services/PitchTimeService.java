package com.pitchmanagement.services;

import com.pitchmanagement.dtos.requests.pitch_time.CreatePitchTimeRequest;
import com.pitchmanagement.dtos.requests.pitch_time.UpdatePitchTimeRequest;
public interface PitchTimeService {
    void createPitchTime(CreatePitchTimeRequest createPitchTimeRequest) throws Exception;
    void updatePitchTime(UpdatePitchTimeRequest updatePitchTimeRequest) throws Exception;
}
