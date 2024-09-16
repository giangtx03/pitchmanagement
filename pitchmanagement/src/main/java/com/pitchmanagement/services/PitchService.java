package com.pitchmanagement.services;

import com.pitchmanagement.models.requests.image.CreateImageRequest;
import com.pitchmanagement.models.requests.pitch.CreatePitchRequest;
import com.pitchmanagement.models.requests.pitch.UpdatePitchRequest;
import com.pitchmanagement.models.responses.PageResponse;
import com.pitchmanagement.models.responses.pitch.PitchResponse;

public interface PitchService {
    PitchResponse createPitch(CreatePitchRequest request) throws Exception;
    PageResponse getAll(String keyword,int startPrice, int endPrice,
                        int starRange, long[] pitchTypes,
                        int pageNumber, int limit, String orderBy, String orderSort);
    PitchResponse getPitchById(Long id) throws Exception;
    PitchResponse updatePitch(UpdatePitchRequest updatePitchRequest) throws Exception;
    void addImages(CreateImageRequest imageRequest) throws Exception;
    void deleteImage(String name);
}
