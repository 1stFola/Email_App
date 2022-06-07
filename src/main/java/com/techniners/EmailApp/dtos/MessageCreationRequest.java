package com.techniners.EmailApp.dtos;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageCreationRequest {
    private String receiverEmail;
    private String subject;
    private String text;
    
    
}
