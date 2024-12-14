package com.example.demo.service;


import com.example.demo.models.uploads.DownloadReq;
import com.example.demo.models.uploads.DownloadResponse;
import com.example.demo.models.uploads.FileResponse;
import com.example.demo.models.uploads.FileUpload;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class WebClientService {

    @Value("${spring.communication.url}")
    private String url;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CryptoEncodingService encodingService;

    public FileResponse sendFile(MultipartFile file,String fileID)  {
        String encodedKey;
        FileResponse response=new FileResponse();
        byte[] fileByte;
        try{
            fileByte=file.getBytes();
            encodedKey=encodingService.encode();
        }
        catch (Exception e){
            System.out.println("Encoding error");
            response.setSuccess(false);
            return response;
        }
        FileUpload body=new FileUpload(fileID,
                Base64.getEncoder().encodeToString(fileByte)
                );
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authentication", "enc "+encodedKey);

        HttpEntity<FileUpload> httpEntity=new HttpEntity<>(body,headers);

        try{
            ResponseEntity<FileResponse> responseEntity=restTemplate.exchange(
                    url+"/upload",
                    HttpMethod.POST,
                    httpEntity,
                    FileResponse.class
            );
            System.out.println(responseEntity);
            return responseEntity.getBody();
        }
        catch (Exception e){
            System.out.println("Network error");
            System.out.println(e.toString());
            response.setSuccess(false);
            return response;
        }
    }

    public DownloadResponse download(String fileId){
        DownloadResponse response=new DownloadResponse();
        RestTemplate template=new RestTemplate();
        String encodedKey;
        try{

            encodedKey=encodingService.encode();
        }
        catch (Exception e){
            System.out.println("Encoding error");
            response.setSuccess(false);
            return response;
        }
        DownloadReq body=new DownloadReq(fileId);
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authentication", "enc "+encodedKey);
        HttpEntity<DownloadReq> httpEntity=new HttpEntity<>(body,headers);
        try{
            ResponseEntity<DownloadResponse> mainResponse=
                    template.exchange(
                            url+"/download",
                            HttpMethod.POST,
                            httpEntity,
                            DownloadResponse.class
                    );
            System.out.println(mainResponse.getStatusCode() + " " + mainResponse.getBody().isSuccess());
            if (mainResponse.getStatusCode()==HttpStatus.OK && mainResponse.getBody()!=null){
                return mainResponse.getBody();
            }
            response.setSuccess(false);
            return response;
        }
        catch (Exception e){
            System.out.println(e.toString() + "adm");
            response.setSuccess(false);
            return response;
        }
    }
}
