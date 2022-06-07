package com.techniners.EmailApp.data.repositories;

import com.techniners.EmailApp.data.models.Notification;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    Optional<Notification> findByEmailAddress(String userName);

}

