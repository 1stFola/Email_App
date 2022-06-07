package com.techniners.EmailApp.dtos.requests.userCenteredRequests;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class LoginRequest {

    private String email;
    private String password;

}
