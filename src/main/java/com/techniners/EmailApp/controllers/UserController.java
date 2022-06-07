package com.techniners.EmailApp.controllers;


import com.techniners.EmailApp.controllers.responses.ApiResponse;
import com.techniners.EmailApp.data.models.User;
import com.techniners.EmailApp.dtos.requests.userCenteredRequests.AccountCreationRequest;
import com.techniners.EmailApp.dtos.UserDTO;
import com.techniners.EmailApp.exceptions.EmailAppException;
import com.techniners.EmailApp.services.UserService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RestController
@NoArgsConstructor
@RequestMapping("/api/v1/emailapp")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    PasswordConfig passwordConfig;



    @PostMapping("/users/createUser")
    public ResponseEntity<?> createUserAccount(@RequestBody @Valid @NotNull @NotBlank AccountCreationRequest accountCreationRequest) throws EmailAppException {
//        accountCreationRequest.setPassword(passwordConfig.passwordEncoder().encode(accountCreationRequest.getPassword()));
        UserDTO userDTO = userService.createAccount(accountCreationRequest);
        ApiResponse response = ApiResponse.builder()
             .payload(userDTO)
             .message("Account Created Successfully")
             .isSuccessful(true)
             .statusCode(201)
             .build();
     return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

//    @PostMapping("/users/login")
//    public ResponseEntity<?> loginToPage(@RequestParam String Email, String password) throws EmailAppException {
//        User user = userService.findByUserName(Email);
//        String pass = user.getPassword();
//        if( pass.equals(password)){
//            return new ResponseEntity<>(user.getId(), HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//    }


    @DeleteMapping("/users/delete{userId}")
    public ResponseEntity<?> delete(@PathVariable("userId") String userId) throws EmailAppException {
        userService.deleteUser(userId);

        ApiResponse apiResponse = ApiResponse.builder()
                .isSuccessful(true)
                .message("User Deleted Successfully")
                .statusCode(200)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }

    @GetMapping("/users/all")
    public ResponseEntity<?> getAllUsers(){
        List<User> users = userService.getAllUsers();
        ApiResponse apiResponse = ApiResponse.builder()
                .payload(users)
                .message("user returned successfully")
                .statusCode(200)
                .isSuccessful(true)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException exception) {
//        Map<String, String> errors = new HashMap<>();
//        exception.getBindingResult().getAllErrors().forEach((error -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        }));
//        return errors;
//    }

    }
