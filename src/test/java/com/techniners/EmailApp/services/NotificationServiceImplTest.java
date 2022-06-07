package com.techniners.EmailApp.services;

import com.techniners.EmailApp.data.models.Message;
import com.techniners.EmailApp.dtos.requests.userCenteredRequests.AccountCreationRequest;
import com.techniners.EmailApp.dtos.UserDTO;
import com.techniners.EmailApp.exceptions.EmailAppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ImportAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
class NotificationServiceImplTest {


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

    @Test
    void userCanReadNotificationTest() throws EmailAppException {
        Message messageSent = newUserSendMessage1();
        List<Message> messages = notificationService.viewAllNotificationMessages(messageSent.getReceiverEmail());
        Message dMessage = null;
        for (Message message: messages) {
            if (messageSent.getId().equals(message.getId())){
                dMessage = message;
            }
        }
        assert dMessage != null;
        assertEquals(dMessage.getSubject(), "I miss my kids" );

    }

    @Test
    void viewAllMessagesInTheNotificationBoxTest() throws EmailAppException {
        Message messageSent = newUserSendMessage1();
        List<Message> messages = notificationService.viewAllNotificationMessages(messageSent.getReceiverEmail());

        assertEquals(messages.size(),1 );
    }


}