package com.example.demo.Repo;

import com.example.demo.Document.FileMetaDataStorage;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface FileMetaDataStorageRepo extends MongoRepository<FileMetaDataStorage,String> {
}
