package com.techniners.EmailApp.dtos.requests.userCenteredRequests;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreationRequest {
    private String userName;
    private String firstName;
    private String lastName;
    private String password;
}
