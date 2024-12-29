package com.pitchmanagement.daos;

import com.pitchmanagement.models.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationDAO {
    void insert(Notification notification);
    List<Notification> getAllByUserId(@Param("userId") Long userId);
    int countUnreadByUserId(@Param("userId") Long userId);
    void readAllByUserId(@Param("userId") Long userId);
    void deleteAllByUserId(@Param("userId") Long userId);
}
