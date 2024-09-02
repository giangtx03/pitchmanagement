package com.pitchmanagement.services.impl;

import com.pitchmanagement.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    @Value("${images.path}")
    private String path;

    @Override
    public String upload(MultipartFile file) throws Exception {

        if(file == null || file.isEmpty()){
            throw new RuntimeException("File rỗng");
        }

        if(file.getContentType() == null || !file.getContentType().startsWith("image/")){
            throw new InvalidPropertiesFormatException("Không phải file ảnh");
        }
        String contentType = file.getContentType().substring(6);
        String uniqueFilename = UUID.randomUUID().toString() + "." + contentType;

        Path uploadDir = Paths.get(path);

        if(!Files.exists(uploadDir)){
            Files.createDirectory(uploadDir);
        }

        Path destination = Paths.get(uploadDir.toString(), uniqueFilename);

        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFilename;
    }

    @Override
    public Resource download(String filename) throws Exception {
        Path fileSrc = Paths.get(path + "/" + filename);
        if(!Files.exists(fileSrc)){
            throw new FileNotFoundException("Không tìm thấy file " + filename);
        }
        return new UrlResource(fileSrc.toUri());
    }

    @Override
    public void delete( String filename) throws IOException {
        Path fileSrc = Paths.get(path + "/" + filename);
        Files.deleteIfExists(fileSrc);
    }
}