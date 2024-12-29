package com.pitchmanagement.services;

import com.pitchmanagement.dtos.requests.image.CreateImageRequest;
import com.pitchmanagement.dtos.requests.pitch.CreatePitchRequest;
import com.pitchmanagement.dtos.requests.pitch.UpdatePitchRequest;
import com.pitchmanagement.dtos.responses.PageResponse;
import com.pitchmanagement.dtos.responses.pitch.PitchResponse;

public interface PitchService {
    PitchResponse createPitch(CreatePitchRequest request) throws Exception;
    PageResponse getAll(String keyword,int startPrice, int endPrice,
                        Long managerId,
                        int starRange, long[] pitchTypes,
                        boolean requestQuery,
                        int pageNumber, int limit, String orderBy, String orderSort);
    PitchResponse getPitchById(Long id, boolean requestQuery) throws Exception;
    PitchResponse updatePitch(UpdatePitchRequest updatePitchRequest) throws Exception;
    void addImages(CreateImageRequest imageRequest) throws Exception;
    void deleteImage(String name);
}
