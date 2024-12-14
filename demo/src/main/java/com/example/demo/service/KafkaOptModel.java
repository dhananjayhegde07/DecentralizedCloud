package com.example.demo.service;


import lombok.Data;

@Data
public class KafkaOptModel {
    private String otp;
    private String sendto;
    private String validationID;
}
