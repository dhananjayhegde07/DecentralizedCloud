package com.example.demo.Document;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class OptValidation {
    @Id
    private String validationID;
    private String otpsentTO;
    private String opt;
}
