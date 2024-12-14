package com.Data.DataHandler.Service;


import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class FilterService {

    @Value("${spring.communication.key}")
    String SECRET_KEY;

    public String getKeyFromHeader(HttpServletRequest req){
        String key=req.getHeader("Authentication");

        if (key==null || !key.startsWith("enc ")) return null;
        return key.substring(4);
    }

    public boolean ValidateKey(String key) throws IllegalBlockSizeException, BadPaddingException,
            InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
            SecretKeySpec keySpec=new SecretKeySpec(SECRET_KEY.getBytes(),"AES");
            Cipher cipher=Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE,keySpec);
            byte[] decodedKey=cipher.doFinal(Base64.getDecoder().decode(key));
            return new String(decodedKey).equals(SECRET_KEY);
    }
}
