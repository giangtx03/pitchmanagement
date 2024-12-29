package com.pitchmanagement.daos;

import com.pitchmanagement.models.Image;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ImageDao {

    void insertImage(Image imageDto);
    List<Image> getAllByPitchId(@Param("pitchId") Long pitchId);
    void deleteImage(@Param("imageName") String imageName);

}
