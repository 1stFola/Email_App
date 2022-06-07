package com.techniners.EmailApp.dtos;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageRequest {

    String senderEmail;
    String receiverEmail;
    String subject;
    String text;
    LocalDateTime localDateTime;

}
