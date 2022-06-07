package com.techniners.EmailApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendToManyDTO {

    private String senderEmail;
    private String message;
    private String subject;
    private String[] receivers;
}
