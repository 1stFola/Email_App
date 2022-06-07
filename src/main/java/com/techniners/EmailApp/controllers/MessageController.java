package com.techniners.EmailApp.controllers;


import com.techniners.EmailApp.controllers.responses.ApiResponse;
import com.techniners.EmailApp.data.models.Message;
import com.techniners.EmailApp.data.models.User;
import com.techniners.EmailApp.dto.SendToManyDTO;
import com.techniners.EmailApp.dtos.SendMessageRequest;
import com.techniners.EmailApp.exceptions.EmailAppException;
import com.techniners.EmailApp.services.MailboxesService;
import com.techniners.EmailApp.services.MessageService;
import com.techniners.EmailApp.services.UserService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
@RestController
@NoArgsConstructor
@RequestMapping("/api/v1/emailapp/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    MailboxesService mailboxesService;


    @PostMapping("/createMessage/{senderEmail}")
    public ResponseEntity<?> createMessage(@PathVariable ("senderEmail") String senderEmail, @RequestParam String receiverEmail, String subject, String text) throws EmailAppException {
        String userEmail = messageService.findByEmail(senderEmail);
        Message messageCreated = messageService.sendAMessage(userEmail, receiverEmail, subject, text, LocalDateTime.now());
        ApiResponse apiResponse = ApiResponse.builder()
                .payload(messageCreated)
                .isSuccessful(true)
                .message("Message Created Successfully")
                .statusCode(201)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/sendMessage/{typedMessage}")
    public ResponseEntity<?> sendMessage(@PathVariable ("typedMessage") SendMessageRequest typedMessage) throws EmailAppException {
        User receiver = userService.findByUserName(typedMessage.getReceiverEmail());

        Message messageCreated = messageService.sendAMessage(typedMessage.getSenderEmail(), receiver.getUserName(), typedMessage.getSubject(), typedMessage.getText(), LocalDateTime.now());

        messageService.storeAMessage(messageCreated);
        messageService.storeToSentFolder(messageCreated);
        ApiResponse apiResponse = ApiResponse.builder()
                .payload(messageCreated)
                .isSuccessful(true)
                .message("Message Sent Successfully")
                .statusCode(201)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }



    @GetMapping("/inbox/read/{messageId}")
    public ResponseEntity<?> readAnInboxMessage(@RequestParam String userEmail, @PathVariable("messageId") String messageId) throws EmailAppException {
        User user = userService.findByUserName(userEmail);
        String ownerEmail = user.getUserName();
        Message mail = messageService.viewInboxMessage(ownerEmail, messageId);
        ApiResponse apiResponse = ApiResponse.builder()
            .payload(mail)
            .isSuccessful(true)
            .message("Message viewed")
            .statusCode(201)
            .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
            }

    @GetMapping("/sent/read/{messageId}")
    public ResponseEntity<?> readASentMessage(@RequestParam String userEmail, @PathVariable("messageId") String messageId) throws EmailAppException {
        User sender = userService.findByUserName(userEmail);
        String ownerEmail = sender.getUserName();
        Message mail = messageService.viewOutBoxMessage(ownerEmail, messageId);
        ApiResponse apiResponse = ApiResponse.builder()
                .payload(mail)
                .isSuccessful(true)
                .message("Message viewed")
                .statusCode(201)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/forwardMessage/{messageId}")
    public ResponseEntity<?> forwardMessage (@PathVariable("messageId") String messageId, @RequestBody SendToManyDTO typedMessage) throws EmailAppException {
        Message message = messageService.findMessageById(messageId);
        String[] receivers = typedMessage.getReceivers();

        Arrays.stream(receivers).forEach(receiver->{
            try {
                messageService.forwardMessages(message, receiver);
            } catch (EmailAppException ignored) {

            }
        });
        ApiResponse apiResponse = ApiResponse.builder()
                .payload("Message sent to "+ receivers.length + " contacts successfully")
                .isSuccessful(true)
                .message("Message Sent Successfully")
                .statusCode(201)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

    }

    @PostMapping("/sendToMany")
    public ResponseEntity<?> sendToMany(@RequestBody SendToManyDTO send2ManyDTO) throws EmailAppException {
        String[] receivers = send2ManyDTO.getReceivers();
        messageService.sendManyMessages(send2ManyDTO);

        ApiResponse apiResponse = ApiResponse.builder()
                .payload("Message sent to "+ receivers.length + " successfully")
                .isSuccessful(true)
                .message("Message Sent Successfully")
                .statusCode(201)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

    }

    @DeleteMapping("/delete{messageId}")
    public ResponseEntity<?> deleteMessage (@PathVariable("messageId") String messageId) throws EmailAppException {
        messageService.deleteMessage(messageId);

        ApiResponse apiResponse = ApiResponse.builder()
                .isSuccessful(true)
                .message("Message Deleted Successfully")
                .statusCode(200)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        }));
        return errors;
    }

    }

//        Arrays.stream(receivers).forEach(receiver->{
//            try {
//                messageService.sendManyMessages(typedMessage, receiver);
//            } catch (EmailAppException ignored) {
//
//            }
//        });