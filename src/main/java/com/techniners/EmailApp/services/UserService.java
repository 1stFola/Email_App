package com.techniners.EmailApp.services;

import com.techniners.EmailApp.data.models.User;
import com.techniners.EmailApp.dtos.requests.userCenteredRequests.AccountCreationRequest;
import com.techniners.EmailApp.dtos.UserDTO;
import com.techniners.EmailApp.exceptions.EmailAppException;
import com.techniners.EmailApp.exceptions.UserNotFoundException;

import java.util.List;

public interface UserService {
    UserDTO createAccount(AccountCreationRequest accountCreationrequest) throws EmailAppException;

    //    User findByUserName(String userName) throws EmailAppException;
    User findByUserName(String userName) throws UserNotFoundException;

    void deleteUser(String userId) throws EmailAppException;

    List<User> getAllUsers();

    User findUserByUserName(String userName) throws EmailAppException;

}
