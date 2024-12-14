package com.Data.DataHandler.Service;


import com.Data.DataHandler.model.ServerNode;
import com.Data.DataHandler.repo.ServerNodeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class HealthCheckService {

    @Autowired
    ServerNodeRepo nodeRepo;

    private final RestTemplate template=new RestTemplate();

    @Scheduled(fixedDelay = 5000)
    public void checkHealth(){
        List<ServerNode> nodes = nodeRepo.findAll();
        for (ServerNode node : nodes){
            if (!pingServer(node)){

            }
        }
    }

    private boolean pingServer(ServerNode node){
        try{
            String status=template.getForObject(node.getIp()+"/actuator/health",String.class);
            if (status==null) return false;
            return true;
        }
        catch (Exception e){
            System.out.println(e.toString());
            return false;
        }
    }
}
