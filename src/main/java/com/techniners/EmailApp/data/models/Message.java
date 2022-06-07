package com.techniners.EmailApp.data.models;

import lombok.*;
//import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Entity
@Document
public class Message {
    @Id
    private String id;

    @NonNull
    private String senderEmail;

    @NonNull
    private String receiverEmail;

    @NonNull
    private String subject;

    @NonNull
    private String text;

//    @CreationTimestamp
    private LocalDateTime timeCreated;

    private boolean isRead = false;

    private boolean isDelivered = false;

//    @ManyToOne
//    @JoinColumn(name = "message_id")
    private Notification notification;

    public Message(@NonNull String senderEmail, @NonNull String receiverEmail, @NonNull String subject, @NonNull String text, LocalDateTime timeCreated) {
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.subject = subject;
        this.text = text;
        this.timeCreated = timeCreated;
    }

    public Message(String receiverEmail, String subject, String text, LocalDateTime timeCreated) {
        this.receiverEmail = receiverEmail;
        this.subject = subject;
        this.text = text;
        this.timeCreated = timeCreated;
    }

//        public Message(LocalDateTime timeCreated, String... receiversEmail) {
//
//        }


//    public Message(LocalDateTime timeCreated, String... receiversEmail) {
//        this.receiversEmail =  for (String s : receiversEmail)
//
//        this.timeCreated = timeCreated;
//    }
}
