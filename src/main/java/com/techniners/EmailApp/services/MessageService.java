package com.techniners.EmailApp.services;

import com.techniners.EmailApp.data.models.Message;
import com.techniners.EmailApp.dto.SendToManyDTO;
import com.techniners.EmailApp.exceptions.EmailAppException;

import java.time.LocalDateTime;
import java.util.List;


public interface MessageService {
//    Message createMessage(String userEmail, String receiverEmail, String subject, String text, LocalDateTime now) throws EmailAppException;

    String findByEmail(String senderEmail);

    void storeAMessage(Message messageCreated) throws EmailAppException;

    Message sendAMessage(String userEmail, String receiverEmail, String subject, String text, LocalDateTime now) throws EmailAppException;

    void storeToSentFolder(Message createdMessage) throws EmailAppException;

    Message viewInboxMessage(String ownerEmail, String messageId) throws EmailAppException;

    Message viewOutBoxMessage(String ownerEmail, String messageId) throws EmailAppException;

    Message findMessageById(String messageId);

    void sendManyMessages(SendToManyDTO typedMessage) throws EmailAppException;

    void forwardMessages(Message message, String receiver) throws EmailAppException;

    void deleteMessage(String messageId) throws EmailAppException;

    List<Message> viewMessagesInInbox(String ownerEmail) throws EmailAppException;

    List<Message> viewMessagesInSentFolder(String ownerEmail) throws EmailAppException;

    void forwardMessageToMany(SendToManyDTO send2ManyDTO, Message forwardedMessage) throws EmailAppException;
    }