package com.example.demo.controller;

import com.example.demo.Document.OtpRequest;
import com.example.demo.Document.User;
import com.example.demo.Repo.Userrepo;
import com.example.demo.jwt.JwtUtils;
import com.example.demo.models.Signup;
import com.example.demo.service.KafkaService;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.Login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@Validated
public class LoginSignup {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    KafkaService kafkaService;

    @Autowired
    UserService userService;

    PasswordEncoder encoder=new BCryptPasswordEncoder();

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody Signup data) throws JsonProcessingException {
        if (userService.UsernameExists(data.getUsername())){
            Map<String,String> map=new HashMap<>();
            map.put("Status","Username already exists");
            ResponseEntity<Map<String,String>> responseEntity=
                    new ResponseEntity<>(map,HttpStatus.IM_USED);
            return responseEntity;
        }
        ArrayList<String> list =new ArrayList<>();

        User newUSer=new User(null,data.getUsername(),encoder.encode(data.getPassword()),data.getEmail()
        ,data.getPhone(),data.getDob(),0,0,list,false,null);

        userService.saveUser(newUSer);
        Map<String, String> map=new HashMap<>();
        String uid= kafkaService.SentOtp(newUSer.getEmail());
        map.put("Status","Done");
        map.put("email","Sent");
        map.put("uid",uid);
        map.put("username",newUSer.getUsername());
        return new ResponseEntity<Map<String,String>>(map,HttpStatus.OK);
    }


    @PostMapping("/signup/resendopt")
    public ResponseEntity<?> resend(@Valid @RequestBody Signup data) throws JsonProcessingException {
       if (!userService.canResend(data.getUsername())){
           Map<String,String> map=new HashMap<>();
           map.put("Status","Can't send new opt");
           return new ResponseEntity<>(map,HttpStatus.UNAUTHORIZED);
       }
       String uid = kafkaService.SentOtp(data.getEmail());
       Map<String,String> map=new HashMap<>();
        map.put("Status","Done");
        map.put("email","Sent");
        map.put("uid",uid);
       return new ResponseEntity<>(map,HttpStatus.OK);
    }

    @PostMapping("/signup/authenticate")
    public ResponseEntity<?> authenticate(@Valid @RequestBody OtpRequest req){
        if (!userService.otpValidation(req)){
            Map<String,String> map=new HashMap<>();
            map.put("Status","Invalid");
            return new ResponseEntity<>(map,HttpStatus.NOT_FOUND);
        }
        Map<String,String> map=new HashMap<>();
        map.put("Status","Done");
        userService.EnableUser(req.getUsername());
        return new ResponseEntity<>(map,HttpStatus.OK);
    }

    @GetMapping("/signup/test")
    public ResponseEntity<?> test() throws JsonProcessingException {
        kafkaService.SentOtp("dhananjayhegde7@gmail.com");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/signup/login")
    public ResponseEntity<?> authenticate( @Valid @RequestBody Login login){
        System.out.println(login.getUsername()+ " "+login.getPassword());
        Authentication auth;
        auth=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        login.getUsername(),login.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(auth);
        User principal= (User) auth.getPrincipal();
        String jwtToken=jwtUtils.getJWT_Token(principal);
        return new ResponseEntity<String>(jwtToken,HttpStatus.OK);
    }

}
