package com.Email.EmailService;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KafkaModel {
    private String otp;
    private String sendto;
    private String validationID;
}
