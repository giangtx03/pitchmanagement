package com.pitchmanagement.services;

import com.pitchmanagement.models.Notification;

public interface NotificationService {
    void sendMessageAndCreateNotification(Notification notification) throws Exception;
    void deleteNotificationByRecipientId(Long recipientId);
    void readAllNotificationByRecipientId(Long recipientId);
    int countUnreadByRecipientId(Long recipientId);
}
