package com.techniners.EmailApp.controllers.responses;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthToken {
    private String token;
    private String id;


}
