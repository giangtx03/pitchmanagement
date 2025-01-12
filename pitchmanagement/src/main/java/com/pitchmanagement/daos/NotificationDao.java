package com.pitchmanagement.daos;

import com.pitchmanagement.models.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationDao {
    void insert(Notification notification);
    List<Notification> getAllByRecipientId(@Param("recipientId") Long recipientId);
    int countUnreadByRecipientId(@Param("recipientId") Long recipientId);
    void readAllByRecipientId(@Param("recipientId") Long recipientId);
    void deleteAllByRecipientId(@Param("recipientId") Long recipientId);
}
