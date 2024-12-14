package com.example.demo.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class CryptoEncodingService {

    @Value("${spring.communication.key}")
    private String SECRET_KEY;

    public String encode() throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        SecretKeySpec keySpec=new SecretKeySpec(SECRET_KEY.getBytes(),"AES");
        Cipher cipher=Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE,keySpec);
        byte[] enc=cipher.doFinal(SECRET_KEY.getBytes());
        return Base64.getEncoder().encodeToString(enc);
    }

    public String decode(String encriptedString) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec keySpec=new SecretKeySpec(SECRET_KEY.getBytes(),"AES");
        Cipher cipher=Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE,keySpec);
        byte[] decodedKey=cipher.doFinal(Base64.getDecoder().decode(encriptedString));
        return new String(decodedKey);
    }
}
