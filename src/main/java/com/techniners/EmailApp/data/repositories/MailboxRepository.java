package com.techniners.EmailApp.data.repositories;

import com.techniners.EmailApp.data.models.MailBox;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailboxRepository extends MongoRepository<MailBox, String> {
       MailBox findByUserName(String userName);
}
