package com.pitchmanagement.daos;

import com.pitchmanagement.dtos.ImageDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ImageDao {

    void insertImage(ImageDto imageDto);
    List<ImageDto> getAllByPitchId(@Param("pitchId") Long pitchId);
    void deleteImage(@Param("imageName") String imageName);

}
