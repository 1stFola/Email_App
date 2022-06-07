package com.techniners.EmailApp.services;

import com.techniners.EmailApp.data.models.AccessToken;
import com.techniners.EmailApp.data.models.User;
import com.techniners.EmailApp.dtos.requests.userCenteredRequests.LoginRequest;
import com.techniners.EmailApp.exceptions.EmailAppException;
import com.techniners.EmailApp.security.jwt.TokenServices;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {


    @Autowired
    UserServiceImpl userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    TokenServices tokenServices;



    @Override
    public AccessToken login(LoginRequest loginRequest) throws EmailAppException {
        User user = userService.findByUserName(loginRequest.getEmail());
        log.info("req --> {}", loginRequest);
        log.info("user --> {}", user);
        log.info("test --> {}",passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()) );
        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.info("match");
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
            log.info("about to generate token");
            String token = tokenServices.generateToken(userDetails);
            return new AccessToken(token);
        }
        throw new EmailAppException("Invalid Password");
    }
}



//
//    @Override
//    public AccessToken login(LoginRequest loginRequest) throws EmailAppException {
//        User user = userService.findByUserName(loginRequest.getEmail());
//        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
//            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
//            String token = tokenServices.generateToken(userDetails);
//            return new AccessToken(token);
//        }
//        log.info("database password ------------>" + user.getPassword());
//        log.info("user password------------>" + loginRequest.getPassword());
//        throw new EmailAppException("Invalid Password");
//    }
//}



 /*   @Override
    public boolean login(String userEmail, String password) throws EmailAppException {
        User user = userService.findByUserName(userEmail);
        String pass = user.getPassword();
        if( pass.equals(password)) {
            return true;
        }

        return false;
    }*/