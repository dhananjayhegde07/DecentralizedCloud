package com.example.demo.controller;


import com.example.demo.Document.FileMetaDataStorage;
import com.example.demo.Document.User;
import com.example.demo.Repo.FileMetaDataStorageRepo;
import com.example.demo.Repo.Userrepo;
import com.example.demo.models.uploads.DownloadReqFront;
import com.example.demo.models.uploads.DownloadResponse;
import com.example.demo.models.uploads.FileResponse;
import com.example.demo.service.CryptoEncodingService;
import com.example.demo.service.FileMetaDataStorageService;
import com.example.demo.service.UserService;
import com.example.demo.service.WebClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@Validated
public class WebFiles {
    @Value("${spring.communication.key}")
    private String xx;

    @Autowired
    CryptoEncodingService encodingService;

    @Autowired
    FileMetaDataStorageService metaDataStorageService;

    @Autowired
    UserService userService;

    @Autowired
    WebClientService webClientService;

    @PostMapping("/web/test")
    public String validate(){
        try{
            return encodingService.encode();
        }catch (Exception e){
            System.out.println(e.toString());
            return "false";
        }
    }

    @PostMapping("/web/decode")
    public String decode(@RequestBody Map<String ,String> key){
        try{
            String decode=encodingService.decode(key.get("key"));
            System.out.println(decode.equals(xx));
            return "c";
        }
        catch (Exception e){
            return "false";
        }
    }

    @PostMapping("/web/upload")
    public ResponseEntity<?> uploadFile(@Valid @RequestPart("file") MultipartFile upload) {
        System.out.println("Called"+ " "+ upload.getOriginalFilename());
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        User user=(User) auth.getPrincipal();
        FileMetaDataStorage file =metaDataStorageService.SaveFileMetaData(
                null,user.getUsername(),upload);
        if (file==null){
            return getErrResponse("DataBase error","err_102",HttpStatus.SERVICE_UNAVAILABLE);
        }
        // performing request and response in normal way
        FileResponse response=webClientService.sendFile(upload,file.getFileid());
        if (!response.isSuccess()){
            metaDataStorageService.Delete(file.getFileid());
            System.out.println("After WebClient Call - Authentication: " + SecurityContextHolder.getContext().getAuthentication());
            System.out.println("not success return");
            return  getErrResponse("Unable to store",
                    "err_101",
                    HttpStatus.BAD_REQUEST);
        }
        user.addToList(file.getFileid());
        user.setTotalFiles(user.getTotalFiles()+1);
        long totalData = file.getFileSize() / 1024 * 1024;
        user.setDataMB(user.getDataMB() + totalData);
        userService.saveChanges(user);
        return new ResponseEntity<Map<String,Object>>
                (Map.of("status","done","code","done_101"),HttpStatus.OK);
    }

    @PostMapping("/web/download")
    public ResponseEntity<?> downloadFile(@Valid @RequestBody DownloadReqFront req){
        FileMetaDataStorage file = metaDataStorageService.find(req.getFileId());
        System.out.println(file);
        DownloadResponse response=webClientService.download(req.getFileId());
        if (!response.isSuccess()){
            return getErrResponse("no response","err_105",HttpStatus.NOT_FOUND);
        }
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(Map.of("file",response.getFile(),"name",file.getFileName()),headers,HttpStatus.OK);
    }

    private ResponseEntity<?> getErrResponse(String msg,String customCode,HttpStatus status){
        Map<String,String> map=new HashMap<>();
        map.put("status",msg);
        map.put("code",customCode);
        return new ResponseEntity<Map<String,String>>(map,status);
    }

}
