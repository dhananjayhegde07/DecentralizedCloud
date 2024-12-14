package com.example.demo.service;

import com.example.demo.Document.FileMetaDataStorage;
import com.example.demo.Repo.FileMetaDataStorageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class FileMetaDataStorageService {
    @Autowired
    FileMetaDataStorageRepo metaDataStorageRepo;

    public FileMetaDataStorage SaveFileMetaData(String fileid, String username, MultipartFile upload){
        try{
            FileMetaDataStorage newfile=new FileMetaDataStorage(
                    fileid,
                    username,
                    LocalDate.now(),
                    upload.getOriginalFilename(),
                    upload.getSize(),
                    upload.getContentType(),
                    "none"
            );

            return metaDataStorageRepo.insert(newfile);
        }
        catch (Exception e){
            return null;
        }
    }

    public void saveChanges(FileMetaDataStorage file){
        metaDataStorageRepo.save(file);
    }

    public void Delete(String id){
        metaDataStorageRepo.deleteById(id);
    }

    public FileMetaDataStorage find(String fileId){
        Optional<FileMetaDataStorage> res = metaDataStorageRepo.findById(fileId);
        return res.orElse(null);
    }
}
