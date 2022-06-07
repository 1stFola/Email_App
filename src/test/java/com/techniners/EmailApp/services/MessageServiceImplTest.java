package com.techniners.EmailApp.services;

import com.techniners.EmailApp.data.models.MailBox;
import com.techniners.EmailApp.data.models.Mailboxes;
import com.techniners.EmailApp.data.models.Message;
import com.techniners.EmailApp.dto.SendToManyDTO;
import com.techniners.EmailApp.dtos.requests.userCenteredRequests.AccountCreationRequest;
import com.techniners.EmailApp.dtos.UserDTO;
import com.techniners.EmailApp.exceptions.EmailAppException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ImportAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
public class MessageServiceImplTest {

    @BeforeEach
    void setUp() {
        messageService.clear();
        mailboxesService.clear();
        userService.clear();
        notificationService.deleteAll();
    }

    @Autowired
    NotificationService notificationService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    MessageServiceImpl messageService;


    @Autowired
    MailboxesServiceImpl mailboxesService;

    private Message newUserSendMessage1() throws EmailAppException {
        AccountCreationRequest accountCreationRequest1 = new AccountCreationRequest("fola1", "folabi", "sanni", "Ibalofa");
        AccountCreationRequest accountCreationRequest2 = new AccountCreationRequest("folz", "Afolabi", "Sanni", "Ibalofa2");
        UserDTO user1 = userService.createAccount(accountCreationRequest1);
        UserDTO user2 = userService.createAccount(accountCreationRequest2);
        return messageService.sendAMessage(user1.getUserName(), user2.getUserName(), "I miss my kids", "My regards to the kids. Hugs and Kisses", LocalDateTime.now());
    }

    private Message newUserSendMessage2() throws EmailAppException {
        AccountCreationRequest accountCreationRequest1 = new AccountCreationRequest("fola", "folabi", "sanni", "Ibalofa");
        AccountCreationRequest accountCreationRequest2 = new AccountCreationRequest("folz1", "Afolabi", "Sanni", "Ibalofa2");
        UserDTO user1 = userService.createAccount(accountCreationRequest1);
        UserDTO user2 = userService.createAccount(accountCreationRequest2);
        return messageService.sendAMessage(user1.getUserName(), user2.getUserName(), "I miss my kids", "My regards to the kids. Hugs and Kisses", LocalDateTime.now());
    }

    @Test
    void userCanSendMessageToAnotherUserTest() throws EmailAppException {

        Message messageSent = newUserSendMessage1();

        String messageSentId = messageSent.getId();
        Optional<Message> dMessage = Optional.ofNullable(messageService.findMessageById(messageSentId));
        assertEquals(dMessage.get().getSenderEmail(), "fola1@fmail.com");
        assertEquals(dMessage.get().getReceiverEmail(), "folz@fmail.com");
        Mailboxes mail =  mailboxesService.findMailboxes(dMessage.get().getReceiverEmail());
        MailBox email = mail.getLetterBoxes().get("inbox");
        assertEquals(email.getMessageList().size(), 1);

    }



    @Test
    void userCanRetrieveAMessageToViewTest() throws EmailAppException {
        Message messageSent = newUserSendMessage2();
        Mailboxes recipientBoxes = mailboxesService.findMailboxes(messageSent.getReceiverEmail());
       MailBox recipientInbox = recipientBoxes.getLetterBoxes().get("inbox");
       
       List<Message> messages =  recipientInbox.getMessageList();
        Message dMessage = null;
        for (Message message: messages  ) {
            if (message.getId().equals(messageSent.getId())){
                dMessage = message;
            }
        }
       
       assertEquals(dMessage.getSubject(), "I miss my kids");
       
    }


    @Test
    void messageCanBeViewedInOutBoxTest() throws EmailAppException {
        Message messageSent = newUserSendMessage1();
        Mailboxes senderBoxes = mailboxesService.findMailboxes(messageSent.getSenderEmail());
        MailBox senderOutBox = senderBoxes.getLetterBoxes().get("sent");


        List<Message> messages =  senderOutBox.getMessageList();
        Message dMessage = null;
        for (Message message: messages  ) {
            if (message.getId().equals(messageSent.getId())){
                dMessage = message;
            }
        }

        assertEquals(dMessage.getText(), "My regards to the kids. Hugs and Kisses");

    }

    @Test
    void inboxAndOutBoxFolderCanBeFound() throws EmailAppException {
        Message messageSent = newUserSendMessage2();
        List<Message> messagesInInbox = messageService.viewMessagesInInbox(messageSent.getReceiverEmail());
        assertEquals(messagesInInbox.size(), 1);

        List<Message> messageReceived = messageService.viewMessagesInSentFolder(messageSent.getSenderEmail());
        assertEquals(messageReceived.size(), 1);

    }

    @Test
    void messagesCanBeSentToManyRecipients() throws EmailAppException {
        AccountCreationRequest accountCreationRequest1 = new AccountCreationRequest("fola", "folabi", "sanni", "Ibalofa");
        AccountCreationRequest accountCreationRequest2 = new AccountCreationRequest("folz", "Afolabi", "Sanni", "Ibalofa2");
        AccountCreationRequest accountCreationRequest3 = new AccountCreationRequest("afo", "Toyosi", "Sanni", "Ibalofa3");
        AccountCreationRequest accountCreationRequest4 = new AccountCreationRequest("labi", "zidane", "Sanni", "Ibalofa4");
        UserDTO user1 = userService.createAccount(accountCreationRequest1);
        UserDTO user2 = userService.createAccount(accountCreationRequest2);
        UserDTO user3 = userService.createAccount(accountCreationRequest3);
        UserDTO user4 = userService.createAccount(accountCreationRequest4);

        String[] users = new String[]{user2.getUserName(), user3.getUserName(), user4.getUserName()};

        SendToManyDTO sendToManyDTO = new SendToManyDTO(user1.getUserName(), "My regards to the kids. Hugs and Kisses", "I miss my kids", users);

        messageService.sendManyMessages(sendToManyDTO);

        Mailboxes mailboxes = mailboxesService.findMailboxes("afo@fmail.com");
        MailBox inbox = mailboxes.getLetterBoxes().get("inbox");
        assertEquals(1, inbox.getMessageList().size());
    }


}