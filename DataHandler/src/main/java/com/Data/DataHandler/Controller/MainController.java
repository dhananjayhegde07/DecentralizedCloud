package com.Data.DataHandler.Controller;


import com.Data.DataHandler.Service.ChunkingService;
import com.Data.DataHandler.Service.RepoService;
import com.Data.DataHandler.Service.SendChunksService;
import com.Data.DataHandler.document.FileIdentifier;
import com.Data.DataHandler.document.ReplicasDocument;
import com.Data.DataHandler.model.*;
import com.Data.DataHandler.repo.ReplicasRepo;
import com.Data.DataHandler.repo.ServerNodeRepo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.File;
import java.util.*;

@RestController
public class MainController {

    @Autowired
    ChunkingService chunkingService;

    @Autowired
    SendChunksService sendChunksService;

    @Autowired
    RepoService repoService;

    @Autowired
    ServerNodeRepo serverNodeRepo;

    @Autowired
    ReplicasRepo replicasRepo;

    @PostMapping("/addnode")
    public ResponseEntity<?> addNode(@RequestBody AddNode node){
        System.out.println("sdsa");
        ServerNode newnode=new ServerNode(node.getIp(),null,node.getPriority());
        newnode=serverNodeRepo.insert(newnode);
        List<Map<String,String>> replicas=node.getReplicas();
        for(Map<String,String> obj : replicas){
            ReplicasDocument doc=new ReplicasDocument(null,newnode.getNodeid(),obj.get("ip"),
                    Integer.parseInt(obj.get("priority")));
            replicasRepo.insert(doc);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestBody FileUpload file){
        System.out.println("none" + " " + file.getFileId());
        List<byte[]> chunks= chunkingService.getChunks(Base64.getDecoder().decode(file.getFileContent()));

        for(int i=0;i<chunks.size();i++){
            String ChunkIdentifier=chunkingService.generateObfuscatedChunkIdentifier(file.getFileId(),i);
            if (ChunkIdentifier==null){
                System.out.println("chunk");
                return getErrResponse(file.getFileId(),HttpStatus.BAD_REQUEST);
            }

            byte[] encriptedChunk= chunkingService.encriptedChunk(chunks.get(i));

            if (encriptedChunk==null){
                System.out.println("enc");
                return getErrResponse(file.getFileId(),HttpStatus.BAD_REQUEST);
            }

            ServerNode node= chunkingService.getNode(ChunkIdentifier);
            sendChunksService.send(node,encriptedChunk,ChunkIdentifier);
        }
        repoService.addFileIdentifier(file.getFileId(),chunks.size());
        FileResponse response=new FileResponse(true,file.getFileId());
        return new ResponseEntity<FileResponse>(response,HttpStatus.OK);
    }

    @PostMapping("/download")
    public ResponseEntity<?> getDownload(@RequestBody DownloadReq req){

        String fileId = req.getFileid();

        FileIdentifier fileIdentifier = repoService.getFileIdentifier(fileId);
        System.out.println(fileId +" here");

        if (fileIdentifier==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        long chunkSize=fileIdentifier.getChunkSize();
        List<byte[]> bytes=new ArrayList<>();

        for(int i=0;i<chunkSize;i++){
            String chunkidentifier=chunkingService.generateObfuscatedChunkIdentifier(fileId,i);
            ServerNode node=chunkingService.getNode(chunkidentifier);
            byte[] chunk=sendChunksService.get(chunkidentifier,node);
            byte[] decryptedChunk=chunkingService.decode(chunk);
            bytes.add(decryptedChunk);
        }

        byte[] combined=combineBytes(bytes);

        DownloadResponse response=new DownloadResponse(true,
                Base64.getEncoder().encodeToString(combined),
                "done");

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    private  byte[] combineBytes(List<byte[]> byteList) {
       List<Byte> bytes= new ArrayList<>();
       for(byte[] b : byteList){
           if (b==null) break;
           for(byte bt: b)
               bytes.add(bt);
       }
       byte[] combined=new byte[bytes.size()];
       int index=0;
       for (byte b : bytes){
           combined[index++]=b;
       }
        return combined;
    }


    private ResponseEntity<?> getErrResponse(String fileId,HttpStatus status){

        FileResponse response=new FileResponse();
        response.setSuccess(false);
        response.setFileid(fileId);
        return new ResponseEntity<FileResponse>(response,status);
    }
}
