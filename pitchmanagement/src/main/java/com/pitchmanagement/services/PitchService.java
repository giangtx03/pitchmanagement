package com.pitchmanagement.services;

import com.pitchmanagement.models.requests.CreatePitchRequest;
import com.pitchmanagement.models.responses.PitchResponse;

public interface PitchService {
    PitchResponse createPitch(CreatePitchRequest request) throws Exception;
}
