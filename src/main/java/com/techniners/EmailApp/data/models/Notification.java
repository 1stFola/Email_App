package com.techniners.EmailApp.data.models;


import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.annotation.Id;


import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Validated
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Notification {
    @Id
    private String id;

    private String subject;

    private String emailAddress;

    private List<Message> messagesList = new ArrayList<>();

//    public Notification() {
//        this.messagesList = new ArrayList<>();
//    }

}
