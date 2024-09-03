package com.pitchmanagement.services;

import com.pitchmanagement.models.requests.createPitch.CreatePitchRequest;
import com.pitchmanagement.models.responses.PageResponse;
import com.pitchmanagement.models.responses.pitch.PitchResponse;

public interface PitchService {
    PitchResponse createPitch(CreatePitchRequest request) throws Exception;
    PageResponse getAll(String keyword, Long managerId, int pageNumber, int limit, String orderBy, String orderSort);
}
