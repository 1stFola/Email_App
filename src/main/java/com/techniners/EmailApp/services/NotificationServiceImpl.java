package com.techniners.EmailApp.services;

import com.techniners.EmailApp.data.models.Message;
import com.techniners.EmailApp.data.models.Notification;
import com.techniners.EmailApp.data.repositories.NotificationRepository;
import com.techniners.EmailApp.exceptions.EmailAppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    MessageServiceImpl messageService;


    @Override
    public void sendNotificationTo( Message createdMessage) throws EmailAppException {
        Notification notification = findUserNotification(createdMessage.getReceiverEmail());
        List<Message> notificationList = notification.getMessagesList();
        notificationList.add(createdMessage);
        saveNotification(notification);

    }

    public Message readNotification(String ownerEmail, String messageId) throws EmailAppException {
        Notification notification = findUserNotification(ownerEmail);

        for ( Message message : notification.getMessagesList()) {
            if (message.getId().equals(messageId)){
                message.setRead(true);
                messageService.storeAMessage(message);
                return message;
            }
        }
            return null;
    }



    @Override
    public List<Message> viewAllNotificationMessages(String userEmail) throws EmailAppException {
        Notification notification = findUserNotification(userEmail);
        return notification.getMessagesList();
    }


    @Override
    public Notification findUserNotification(String receiverEmail) throws EmailAppException {
        Notification notification = notificationRepository.findByEmailAddress(receiverEmail)
                .orElseThrow(()-> new EmailAppException("Not Found"));
        return notification;
    }

    @Override
    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    public void deleteAll() {
        notificationRepository.deleteAll();
    }

    @Override
    public void deleteNotification(String userName) throws EmailAppException {
        Notification notification = notificationRepository.findByEmailAddress(userName).orElseThrow(()-> new EmailAppException("User not found"));
        notificationRepository.delete(notification);
    }




}
