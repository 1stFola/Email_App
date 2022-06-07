package com.techniners.EmailApp.services;

import com.techniners.EmailApp.data.models.User;
import com.techniners.EmailApp.data.repositories.UserRepository;
import com.techniners.EmailApp.dtos.requests.userCenteredRequests.AccountCreationRequest;
import com.techniners.EmailApp.dtos.UserDTO;
import com.techniners.EmailApp.exceptions.EmailAppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ImportAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
class UserServiceImplTest {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }


    @Test
    void userCanBeCreatedTest() throws EmailAppException {
        AccountCreationRequest accountCreationRequest = new AccountCreationRequest("fola", "folabi", "sanni", "Ibalofa" );
        UserDTO newUser = userService.createAccount(accountCreationRequest);
        Optional<User> user = userRepository.findByUserName(newUser.getUserName());
        assertEquals(user.get().getUserName(), "fola@fmail.com");

    }

   @Test
    void twoUsersCanBeCreatedTest() throws EmailAppException {
       AccountCreationRequest accountCreationRequest = new AccountCreationRequest("fola", "folabi", "sanni", "Ibalofa" );
       AccountCreationRequest accountCreationRequest2 = new AccountCreationRequest("folz", "Afolabi", "Sanni", "Ibalofa2" );
       UserDTO user1 = userService.createAccount(accountCreationRequest);
       UserDTO user = userService.createAccount(accountCreationRequest2);
       assertEquals(userRepository.findAll().size(), 2);

   }

   @Test
    void userCanBeDeleted() throws EmailAppException {
       AccountCreationRequest accountCreationRequest = new AccountCreationRequest("fola", "folabi", "sanni", "Ibalofa" );
       UserDTO newUser = userService.createAccount(accountCreationRequest);
       userService.deleteUser(newUser.getUserName());
       assertEquals(userRepository.findByUserName(newUser.getUserName()), Optional.empty());

   }






}