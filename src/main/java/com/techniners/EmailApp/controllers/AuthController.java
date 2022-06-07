package com.techniners.EmailApp.controllers;


import com.techniners.EmailApp.controllers.responses.Response;
import com.techniners.EmailApp.data.models.Role;
import com.techniners.EmailApp.dtos.requests.userCenteredRequests.LoginRequest;
import com.techniners.EmailApp.exceptions.EmailAppException;
//import com.techniners.EmailApp.security.jwt.TokenProvider;
//import com.techniners.EmailApp.security.jwt.TokenProviderImpl;
import com.techniners.EmailApp.security.jwt.TokenServices;
import com.techniners.EmailApp.services.LoginService;
import com.techniners.EmailApp.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
//@AllArgsConstructor
@RequestMapping("/api/v1/emailapp/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

//    @Autowired
//    private TokenProvider tokenProvider;

    @Autowired
    TokenServices tokenServices;

//    @Autowired
//    private final UserDetails userDetails;

    @Autowired
    PasswordConfig passwordConfig;

    @Autowired
    LoginService loginService;

    @PostMapping("/login")
    public Response login(@RequestBody LoginRequest request) {
        String token;
        try {
            userService.findByUserName(request.getEmail());
            token = loginService.login(request).getAccessToken();
        } catch (EmailAppException e) {
            return new Response (HttpStatus.BAD_REQUEST, false, e.getMessage());
        }
        log.info("This is the token--------->" + token);

        return new Response(HttpStatus.OK, true, token);
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




/*
* - Auth Controller --> CONTROLLER
- Global(Diary Exception Handler) --> CONTROLLER

- Account Creation Request --> DTOs
- Login Request --> DTOs
*
* USER SERVICE IMPLEMENTATION EXTENDS 2 PARENTS
* - public class UserServiceImpl implements UserService, UserDetailsService {

- API Error  --> EXCEPTION
- UserInExistenceException --> EXCEPTION
- UserNotFoundException --> EXCEPTION
*
*  IMPORT THE LAST METHOD ON THIS PAGE FOR THE GLOBAL EXCEPTION HANDLER

        UNDER USER SERVICE IMPLEMENTATION
          private Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles


https://www.javatpoint.com/spring-security-features
- ** --> SECURITY
Configuration Points
- Link to the controller endpoints
*
*
*             log.info("This is the email--------->" + request.getEmail());

*
* */




//    @PostMapping("/login")
//    public ResponseEntity<?> loginToPage(@RequestBody LoginRequest request) throws UserNotFoundException {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
//        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
////        final String token = tokenProvider.generateJWTToken(authentication);
//        final String token = tokenServices.generateToken(userDetails);
//        User user = userService.findByUserName(request.getEmail());
//        return new ResponseEntity<>(new AuthToken(token, user.getId()), HttpStatus.OK);
//    }