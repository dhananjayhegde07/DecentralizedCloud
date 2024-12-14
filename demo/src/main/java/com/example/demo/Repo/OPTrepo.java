package com.example.demo.Repo;

import com.example.demo.Document.OptValidation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OPTrepo extends MongoRepository<OptValidation,String> {

}
