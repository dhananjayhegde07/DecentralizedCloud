package com.example.demo.service;


import com.example.demo.Document.OptValidation;
import com.example.demo.Repo.OPTrepo;
import com.example.demo.models.KafkaModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class KafkaService {

    @Autowired
    OPTrepo otprepo;

    @Autowired
    KafkaTemplate<String, String> template;

    ObjectMapper mapper=new ObjectMapper();

    public void sendToQueue(String model){
        template.send("send-email",model);
    }

    public String SentOtp(String email) throws JsonProcessingException {
        KafkaOptModel model=new KafkaOptModel();
        OptValidation otp=setOtprepo(email);
        model.setValidationID(otp.getValidationID());
        model.setSendto(otp.getOtpsentTO());
        model.setOtp(otp.getOpt());
        template.send("send-email",mapper.writeValueAsString(model));
        return otp.getValidationID();
    }

    private OptValidation setOtprepo(String email){
        OptValidation otp=new OptValidation();
        otp.setOpt(generateOtp());
        otp.setOtpsentTO(email);
        return otprepo.insert(otp);
    }



    private static String generateOtp() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            otp.append(random.nextInt(10)); // Generate a digit from 0-9
        }

        return otp.toString();
    }
}
