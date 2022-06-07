package com.techniners.EmailApp.services;

import com.techniners.EmailApp.data.models.MailBox;
import com.techniners.EmailApp.data.models.Mailboxes;
import com.techniners.EmailApp.data.models.Message;
import com.techniners.EmailApp.data.models.User;
import com.techniners.EmailApp.data.repositories.MessageRepository;
import com.techniners.EmailApp.dto.SendToManyDTO;
import com.techniners.EmailApp.exceptions.EmailAppException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private MailboxesServiceImpl mailboxesService;

    @Autowired
    private NotificationServiceImpl notificationService;

    private Message createMessage(String userEmail, String receiverEmail, String subject, String text, LocalDateTime now) throws EmailAppException {
        User senderEmail = userService.findByUserName(userEmail);
        String senderMail = senderEmail.getUserName();
        Message message = new Message(senderMail, receiverEmail, subject, text, LocalDateTime.now());
      Message savedMessage=  messageRepository.save(message);
        return savedMessage;

    }

    @Override
    public Message sendAMessage(String userEmail, String receiverEmail, String subject, String text, LocalDateTime now) throws EmailAppException {
        Message composedMail = createMessage(userEmail, receiverEmail, subject, text, LocalDateTime.now());

       User user = userService.findByUserName(composedMail.getReceiverEmail());
       Mailboxes mailboxes = mailboxesService.findMailboxes(user.getUserName());

            MailBox receiverInbox = mailboxes.getLetterBoxes().get("inbox");
            List<Message> receiverMessages = receiverInbox.getMessageList();
            composedMail.setDelivered(true);
            receiverMessages.add(composedMail);
            sendNotification(composedMail);
            storeToSentFolder(composedMail);
            Message savedMessage = messageRepository.save(composedMail);
            mailboxesService.saveMailBoxes(mailboxes);
            return savedMessage;

    }

    public void sendManyMessages(SendToManyDTO send2ManyDTO) throws EmailAppException {
        String[] receivers = send2ManyDTO.getReceivers();

        Arrays.stream(receivers).forEach(receiver->{
            try {
              sendAMessage(send2ManyDTO.getSenderEmail(), receiver, send2ManyDTO.getSubject(), send2ManyDTO.getMessage(), LocalDateTime.now());
            } catch (EmailAppException ignored) {

            }
        });
    }

    @Override
    public void forwardMessages(Message forwardedMessage, String receiver) throws EmailAppException {
        sendAMessage(forwardedMessage.getSenderEmail(),forwardedMessage.getReceiverEmail(),"FWD" + forwardedMessage.getSubject(),forwardedMessage.getText(), LocalDateTime.now());

    }

    @Override
    public void forwardMessageToMany(SendToManyDTO send2ManyDTO, Message forwardedMessage) throws EmailAppException {
        send2ManyDTO.setMessage(forwardedMessage.getText());
        send2ManyDTO.setSubject("FWD" + forwardedMessage.getSubject());
        sendManyMessages(send2ManyDTO);
    }



    @Override
    public void storeAMessage(@NotNull Message messageCreated) throws EmailAppException {
        messageRepository.save(messageCreated);
    }

    @Override
    public void storeToSentFolder(Message createdMessage) throws EmailAppException {
        Mailboxes mailboxes = mailboxesService.findMailboxes(createdMessage.getSenderEmail());
        MailBox sentBox = mailboxes.getLetterBoxes().get("sent");
        List<Message> senderMessages = sentBox.getMessageList();
        senderMessages.add(createdMessage);
        mailboxesService.saveMailBoxes(mailboxes);
    }

    @Override
    public Message viewInboxMessage(String ownerEmail, String messageId) throws EmailAppException {
        Mailboxes mailboxes = mailboxesService.findMailboxes(ownerEmail);
        MailBox mailBox = mailboxes.getLetterBoxes().get("inbox");
        for (Message message : mailBox.getMessageList()) {
            if (message.getId().equals(messageId)) {
                message.setRead(true);
                messageRepository.save(message);
                return message;
            }
        }
        return null;
    }

    @Override
    public List<Message> viewMessagesInInbox(String ownerEmail) throws EmailAppException {
        Mailboxes mailboxes = mailboxesService.findMailboxes(ownerEmail);
        MailBox inbox = mailboxes.getLetterBoxes().get("inbox");
        return inbox.getMessageList();
    }

    @Override
    public Message viewOutBoxMessage(String ownerEmail, String messageId) throws EmailAppException {
        Mailboxes mailboxes = mailboxesService.findMailboxes(ownerEmail);
        MailBox mailBox = mailboxes.getLetterBoxes().get("sent");
        for (Message sentMessage : mailBox.getMessageList()) {
            if (sentMessage.getId().equals(messageId)) {
                Message text = mailBox.getMessageList()
                        .stream()
                        .filter(message1 -> message1.getId().equals(messageId))
                        .collect(Collectors.toList()).get(0);
                return text;
            }

        }

        return null;
    }

    @Override
    public List<Message> viewMessagesInSentFolder(String ownerEmail) throws EmailAppException {
        Mailboxes mailboxes = mailboxesService.findMailboxes(ownerEmail);
        MailBox sentBox = mailboxes.getLetterBoxes().get("sent");
        return sentBox.getMessageList();
    }


    @Override
    public Message findMessageById(String messageId) {
        Optional<Message> foundMessage = messageRepository.findById(messageId);
        Message theMessage = foundMessage.get();
        return theMessage;
    }


    @Override
    public void deleteMessage(String messageId) throws EmailAppException {
        Message message = findMessageById(messageId);
        messageRepository.delete(message);

    }

    @SneakyThrows
    @Override
    public String findByEmail(String senderEmail) {
        return String.valueOf(userService.findByUserName(senderEmail));
    }


    public void sendNotification(Message createdMessage) throws EmailAppException {
        notificationService.sendNotificationTo(createdMessage);
    }

    public void clear() {
        messageRepository.deleteAll();
    }
}