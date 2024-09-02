package com.pitchmanagement.services;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.IOException;

public interface ImageService {
    String upload( MultipartFile file) throws  Exception;
    Resource download(String filename) throws Exception;
    void delete( String filename) throws IOException;
}
