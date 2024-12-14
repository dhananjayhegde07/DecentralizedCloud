package com.Data.DataHandler.Service;


import com.Data.DataHandler.model.ServerNode;
import com.Data.DataHandler.repo.ServerNodeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class ChunkingService {
    private int CHUNK_SIZE=1024;

    @Autowired
    ServerNodeRepo serverNodeRepo;

    public List<byte[]> getChunks(byte[] fileBytes){

        if (fileBytes.length < 10 *1024*1024 && fileBytes.length>CHUNK_SIZE) CHUNK_SIZE=512 * 1024;
        else if (fileBytes.length>10 * 1024 * 1024 && fileBytes.length<100 * 1024 * 1024) CHUNK_SIZE=1024 * 1024;
        else CHUNK_SIZE=10 * 1024 *1024;

        List<byte[]> chunks = new ArrayList<>();

        int totalChunks = (int) Math.ceil((double) fileBytes.length / CHUNK_SIZE);

        for (int i = 0; i < totalChunks; i++) {
            int start = i * CHUNK_SIZE;

            int end = Math.min(start + CHUNK_SIZE, fileBytes.length);

            byte[] chunk = new byte[end - start];
            System.arraycopy(fileBytes, start, chunk, 0, end - start);
            chunks.add(chunk);
        }
        return chunks;
    }

    public String generateObfuscatedChunkIdentifier(String fileID, int chunkIndex) {
        String input = fileID + "-" + chunkIndex;
        try{
            return Base64.getUrlEncoder().encodeToString(
                    MessageDigest.getInstance("SHA-256").digest(input.getBytes()));
        }
        catch (Exception e){
            return  null;
        }
    }

    public ServerNode getNode(String chunkIdentifier){
        List<ServerNode> nodes = serverNodeRepo.findAll();
        ConsistantHashingService hashingService=new ConsistantHashingService();
        for (ServerNode node: nodes){
            hashingService.add(node);
        }
        return hashingService.getNode(chunkIdentifier);
    }

    @Value("${spring.communication.key}")
    private String SECRET_KEY;

    public byte[] encriptedChunk(byte[] chunk)  {
        try{
            SecretKeySpec keySpec=new SecretKeySpec(SECRET_KEY.getBytes(),"AES");
            Cipher cipher=Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE,keySpec);
            byte[] enc=cipher.doFinal(chunk);
            return enc;
        }
        catch (Exception e){
            return null;
        }
    }

    public byte[] decode(byte[] encriptedString) {
        try{
            SecretKeySpec keySpec=new SecretKeySpec(SECRET_KEY.getBytes(),"AES");
            Cipher cipher=Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE,keySpec);
            return cipher.doFinal(encriptedString);
        }
        catch (Exception e){
            System.out.println(e.toString());
            return null;
        }
    }
}
