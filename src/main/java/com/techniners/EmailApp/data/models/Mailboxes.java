package com.techniners.EmailApp.data.models;


import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
//@Table(name = "mailboxes")
//@Entity
@Document
public class Mailboxes {
    @Id
    private String userId;

    private String userName;

//    @OneToMany(mappedBy = "mailboxes",
//            orphanRemoval = true,
//            fetch = FetchType.LAZY,
//            cascade = CascadeType.ALL)
//    private List<MailBox> letterBoxes;

    private Map<String, MailBox> letterBoxes;

    public Mailboxes(String userId, String userName, HashMap<String, MailBox> letterBoxes) {
        this.userId = userId;
        this.userName = userName;
        this.letterBoxes = letterBoxes;
        this.letterBoxes.put("inbox", new MailBox(userName, new ArrayList<>(),BoxType.INBOX));
        this.letterBoxes.put("sent", new MailBox(userName, new ArrayList<>(),BoxType.SENT));
    }


}


/*
*  public Mailboxes(String userId, String userName, HashMap<String, MailBox> letterBoxes) {
        this.userId = userId;
        this.userName = userName;
        this.letterBoxes = letterBoxes;
        this.letterBoxes.put("inbox", new MailBox(userName, new ArrayList<>(),BoxType.INBOX));
        this.letterBoxes.put("sent", new MailBox(userName, new ArrayList<>(),BoxType.SENT));
    }
* */