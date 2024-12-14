package com.Data.DataHandler.Service;


import com.Data.DataHandler.model.Chunk;
import com.Data.DataHandler.model.ServerNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class SendChunksService {

    public boolean send(ServerNode node,byte[] chunk , String ChunkIdentifier){
        if( node==null){
            return false;
        }
        RestTemplate template =new RestTemplate();
        Map<String,Object> body=new HashMap<>();
        body.put("chunkID",ChunkIdentifier);
        body.put("chunk", Base64.getUrlEncoder().encodeToString(chunk));
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String,Object>> http=new HttpEntity<>(body,headers);
        ResponseEntity<Void> response=template.exchange(
                node.getIp()+"/save",
                HttpMethod.POST,
                http,
                Void.class
        );
        if (response.getStatusCode()==HttpStatus.OK){
            return true;
        }
        System.out.println(node+ " " +
                "" +chunk + " " + ChunkIdentifier);
        return false;
    }

    public byte[] get(String chunkId,ServerNode node){
        RestTemplate template =new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        System.out.println(node);
        HttpEntity<Map<String,Object>> http=new HttpEntity<>(Map.of("fileId",chunkId),headers);
        ResponseEntity<Chunk> response=template.exchange(
                node.getIp()+"/getChunk",
                HttpMethod.POST,
                http,
                Chunk.class
        );

        System.out.println(response);

        if (response.getStatusCode()==HttpStatus.OK && response.getBody()!=null){
            System.out.println(response);
            String chunk = response.getBody().getChunk();
            return Base64.getUrlDecoder().decode(chunk);
        }

        return null;
    }

}
