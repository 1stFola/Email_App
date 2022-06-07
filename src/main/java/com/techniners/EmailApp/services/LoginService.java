package com.techniners.EmailApp.services;


import com.techniners.EmailApp.data.models.AccessToken;
import com.techniners.EmailApp.dtos.requests.userCenteredRequests.LoginRequest;
import com.techniners.EmailApp.exceptions.EmailAppException;

public interface LoginService {

   AccessToken login(LoginRequest loginRequest) throws EmailAppException;
}
