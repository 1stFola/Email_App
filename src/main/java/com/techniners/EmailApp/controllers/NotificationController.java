package com.techniners.EmailApp.controllers;


import com.techniners.EmailApp.controllers.responses.ApiResponse;
import com.techniners.EmailApp.data.models.Message;
import com.techniners.EmailApp.data.models.Notification;
import com.techniners.EmailApp.data.models.User;
import com.techniners.EmailApp.exceptions.EmailAppException;
import com.techniners.EmailApp.services.NotificationService;
import com.techniners.EmailApp.services.UserService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RestController
@NoArgsConstructor
@RequestMapping("/api/v1/emailapp/notification")
public class NotificationController {

    @Autowired
    UserService userService;

    @Autowired
    NotificationService notificationService;

    @GetMapping("/all/{userEmail}")
    public ResponseEntity<?> getAllMessages(@PathVariable("userEmail") String userEmail) {
        try {
            User foundUser = userService.findByUserName(userEmail);
            Notification notification = notificationService.findUserNotification(foundUser.getUserName());
            List<Message> messages = notification.getMessagesList();

            ApiResponse apiResponse = ApiResponse.builder()
                    .payload(messages)
                    .message("notification messages found")
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
