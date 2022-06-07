package com.techniners.EmailApp.data.models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;


import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
//@Table(name = "mailbox")
//@Entity
@Document
public class MailBox {
    @Id
    private String userName;

//    @OneToMany
    private List<Message> messageList;

//    @ManyToOne
//    @JoinColumn(name = "mailboxes_id")
    private Mailboxes mailboxes;

    @Enumerated(EnumType.STRING)
    private BoxType boxType;

    public MailBox(String userName, List<Message> message, BoxType boxType) {
        this.userName = userName;
        this.messageList = message;
        this.boxType = boxType;
    }
}
