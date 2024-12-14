package com.example.demo.jwt;


import com.example.demo.Document.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class JwtUtils {

    @Value("${spring.app.jwtkey}")
    private String key;

    @Value("${spring.app.jwtexpire}")
    private int expiration;
    
    private String buildjwttoken(Map<String,String> claims, String username){
        return Jwts.builder().claims(claims).subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(getkey())
                .compact();
    }

    private String buildapiJWT(Map<String,String> claims, String username){
        return  Jwts.builder().claims(claims)
                .subject(username)
                .signWith(getkey())
                .compact();
    }

    public String get_Api_Jwt_token(User user){
        Map<String,String> map=new HashMap<>();
        map.put("Email",user.getEmail());
        map.put("phone","5448484215");
        map.put("type","api");
        return buildapiJWT(map,user.getUsername());
    }

    private Key getkey(){
        byte[] newkey=key.getBytes();
        return Keys.hmacShaKeyFor(newkey);
    }

    public String getJWT_Token(User user){
        Map<String, String> map=new HashMap<>();
        map.put("Email",user.getEmail());
        map.put("type","web");
        return buildjwttoken(map,user.getUsername());
    }

    public Claims getclaims(String token){
        return Jwts.parser().verifyWith((SecretKey) getkey())
                .build().parseSignedClaims(token).getPayload();
    }

    public String getJwtfromHeader(HttpServletRequest http){
        String auth=http.getHeader("Authentication");
        if(auth==null || ! auth.startsWith("bearer")) return null;
        return auth.substring(7);
    }

    public Boolean validateJwt(String token){
        try{
            Jwts.parser().verifyWith((SecretKey) getkey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception e){
            System.out.println(e.toString());
            System.out.println("Invalid token");
        }
        return false;
    }

    public String getUsernameFromJWT(String token){
        return  Jwts.parser().verifyWith((SecretKey) getkey()).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }
}
