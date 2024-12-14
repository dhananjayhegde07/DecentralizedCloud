package com.Email.EmailService;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    @Autowired
    private EmailSendingService service;

    @KafkaListener( topics = "send-email")
    public void recieved(String msg) throws JsonProcessingException {
        System.out.println(msg);
        ObjectMapper mapper=new ObjectMapper();
        KafkaModel model=mapper.readValue(msg,KafkaModel.class);
        service.sendMail(model);
    }
}
