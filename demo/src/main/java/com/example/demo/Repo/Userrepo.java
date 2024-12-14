package com.example.demo.Repo;

import com.example.demo.Document.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Userrepo extends MongoRepository<User,String> {
    User findByUsername(String username);
}
