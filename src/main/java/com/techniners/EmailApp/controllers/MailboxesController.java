package com.techniners.EmailApp.controllers;


import com.techniners.EmailApp.controllers.responses.ApiResponse;
import com.techniners.EmailApp.data.models.MailBox;
import com.techniners.EmailApp.data.models.Mailboxes;
import com.techniners.EmailApp.data.models.Message;
import com.techniners.EmailApp.data.models.User;
import com.techniners.EmailApp.exceptions.EmailAppException;
import com.techniners.EmailApp.services.MailboxesService;
import com.techniners.EmailApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController

@RequestMapping("/api/v1/emailapp/mailboxes")
public class MailboxesController {

//    @Autowired
//    UserRepository userRepository;

//    @Autowired
//    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    MailboxesService mailboxesService;


    @GetMapping("/inbox/all/{userEmail}")
    public ResponseEntity<?> getAllMessages(@PathVariable("userEmail") String userEmail) throws EmailAppException {
        try {
           User foundUser = userService.findByUserName(userEmail);
            Mailboxes mailboxes = mailboxesService.findMailboxes(foundUser.getUserName());
            MailBox mailBox = mailboxes.getLetterBoxes().get(0);
            List<Message> messages = mailBox.getMessageList();

            ApiResponse apiResponse = ApiResponse.builder()
                    .payload(messages)
                    .message("successfully found messages")
                    .isSuccessful(true)
                    .statusCode(201)
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);

        }catch (EmailAppException e){
            ApiResponse apiResponse = ApiResponse.builder()
                    .message(e.getMessage())
                    .isSuccessful(false)
                    .statusCode(404)
                    .build();

            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping("/sent/all/{userEmail}")
    public ResponseEntity<?> getAllSentMessages(@PathVariable("userEmail") String userEmail) throws EmailAppException {
        try {
            User foundUser = userService.findByUserName(userEmail);
            Mailboxes mailboxes = mailboxesService.findMailboxes(foundUser.getUserName());
            MailBox mailBox = mailboxes.getLetterBoxes().get(1);
            List<Message> messages = mailBox.getMessageList();

            ApiResponse apiResponse = ApiResponse.builder()
                    .payload(messages)
                    .message("successfully found mesages")
                    .isSuccessful(true)
                    .statusCode(201)
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);

        } catch (EmailAppException e) {
            ApiResponse apiResponse = ApiResponse.builder()
                    .message(e.getMessage())
                    .isSuccessful(false)
                    .statusCode(404)
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);

        }
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
