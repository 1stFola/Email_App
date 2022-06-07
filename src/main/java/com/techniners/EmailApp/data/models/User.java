package com.techniners.EmailApp.data.models;


import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Validated
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@Table(name = "user")
//@Entity
@Document(collection = "user")
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String userName;

    private String firstName;

    private String lastName;

    private String password;

//    @CreationTimestamp
    private LocalDateTime registrationTime;

    private Notification notificationList;
    private Set<Role>roles;


    public User(String userName, String firstName, String lastName, String password, LocalDateTime registrationTime) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.registrationTime = registrationTime;
//        this.notificationList = new ArrayList<>();
    }

    public User(String adminEmail, String adminPassword) {
    }

    public void addRole(Role role) {
        if (this.roles == null){
            this.roles = new HashSet<>();
        }
//        roles.add(role);
    }
}
