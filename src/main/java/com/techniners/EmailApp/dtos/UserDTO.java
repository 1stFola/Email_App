package com.techniners.EmailApp.dtos;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UserDTO {

    private String userName;
    private String firstName;
    private String lastName;


}
