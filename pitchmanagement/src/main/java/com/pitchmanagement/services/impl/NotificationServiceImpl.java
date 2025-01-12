package com.pitchmanagement.services.impl;

import com.pitchmanagement.daos.NotificationDao;
import com.pitchmanagement.daos.UserDao;
import com.pitchmanagement.models.Notification;
import com.pitchmanagement.models.User;
import com.pitchmanagement.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationDao notificationDAO;
    private final UserDao userDao;

    @Override
    public void sendMessageAndCreateNotification(Notification notification) throws Exception {
        User senderUser = userDao.getUserById(notification.getSenderId());
        User recipentUser = userDao.getUserById(notification.getRecipientId());


    }


    @Override
    public void deleteNotificationByRecipientId(Long recipientId) {

    }

    @Override
    public void readAllNotificationByRecipientId(Long recipientId) {

    }

    @Override
    public int countUnreadByRecipientId(Long recipientId) {
        return notificationDAO.countUnreadByRecipientId(recipientId);
    }
}
