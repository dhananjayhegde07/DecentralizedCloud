package com.Data.DataHandler.repo;

import com.Data.DataHandler.document.ReplicasDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReplicasRepo extends MongoRepository<ReplicasDocument,String> {
    List<ReplicasDocument> findByNodeid(String id);
}
