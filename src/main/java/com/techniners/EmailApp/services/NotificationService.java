package com.techniners.EmailApp.services;

import com.techniners.EmailApp.data.models.Message;
import com.techniners.EmailApp.data.models.Notification;
import com.techniners.EmailApp.exceptions.EmailAppException;

import java.util.List;

public interface NotificationService {

    Notification findUserNotification(String receiverEmail) throws EmailAppException;
    void saveNotification(Notification notification);
    void sendNotificationTo(Message createdMessage) throws EmailAppException;
    Message readNotification(String ownerEmail, String messageId) throws EmailAppException;
    List<Message> viewAllNotificationMessages(String userEmail) throws EmailAppException;
    void deleteNotification(String userName) throws EmailAppException;
    void deleteAll();
}
