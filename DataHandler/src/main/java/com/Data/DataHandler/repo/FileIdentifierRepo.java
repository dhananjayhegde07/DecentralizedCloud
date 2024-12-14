package com.Data.DataHandler.repo;


import com.Data.DataHandler.document.FileIdentifier;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileIdentifierRepo extends MongoRepository<FileIdentifier,String> {
}
