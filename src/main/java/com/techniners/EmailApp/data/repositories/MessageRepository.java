package com.techniners.EmailApp.data.repositories;

import com.techniners.EmailApp.data.models.Message;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {


}
