package com.example.demo.controller;


import com.example.demo.Document.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EnableAPi {

    @Autowired
    UserService userService;


    @PostMapping("/api/enable")
    public ResponseEntity<?> enableapi(){
        User user=(User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String key=userService.setAPIKEY(user.getUsername());
        Map<String,String> map=new HashMap<>();
        map.put("Status","Done");
        map.put("api_key",key);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
