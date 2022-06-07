package com.techniners.EmailApp.data.repositories;

import com.techniners.EmailApp.data.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUserName(String userName);
}



//public interface UserRepository extends JpaRepository<User, Long> {
