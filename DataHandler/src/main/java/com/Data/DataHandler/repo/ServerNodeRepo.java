package com.Data.DataHandler.repo;

import com.Data.DataHandler.model.ServerNode;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServerNodeRepo extends MongoRepository<ServerNode, String> {
}
