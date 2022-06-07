package com.techniners.EmailApp.data.repositories;

import com.techniners.EmailApp.data.models.Mailboxes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MailboxesRepository extends MongoRepository<Mailboxes, String> {
    Optional<Mailboxes> findByUserName(String userName);
}

