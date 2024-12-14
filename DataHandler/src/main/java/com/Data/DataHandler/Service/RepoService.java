package com.Data.DataHandler.Service;


import com.Data.DataHandler.document.FileIdentifier;
import com.Data.DataHandler.repo.FileIdentifierRepo;
import com.Data.DataHandler.repo.ServerNodeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RepoService {
    @Autowired
    private FileIdentifierRepo fileIdentifierRepo;

    public void addFileIdentifier(String fileId,long count){
        fileIdentifierRepo.insert(new FileIdentifier(fileId,count));
    }

    public FileIdentifier getFileIdentifier(String fileId){
        Optional<FileIdentifier> fileIdentifier=fileIdentifierRepo.findById(fileId);
        return fileIdentifier.orElse(null);
    }
}
