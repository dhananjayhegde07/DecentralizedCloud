package com.example.demo.Document;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OtpRequest {

    @NotBlank
    private String validationID;
    @NotBlank
    private String Email;
    @NotBlank
    private String otp;

    @NotBlank
    private String username;
}
