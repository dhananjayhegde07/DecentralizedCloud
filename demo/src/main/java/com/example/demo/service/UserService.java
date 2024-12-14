package com.example.demo.service;


import com.example.demo.Document.OptValidation;
import com.example.demo.Document.OtpRequest;
import com.example.demo.Document.User;
import com.example.demo.Repo.OPTrepo;
import com.example.demo.Repo.Userrepo;
import com.example.demo.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    Userrepo userrepo;

    @Autowired
    OPTrepo opTrepo;

    @Autowired
    JwtUtils jwtUtils;

    public Boolean UsernameExists(String name){
        return userrepo.findByUsername(name)!=null;
    }

    public boolean saveUser(User user){
        try{
            User user1 =userrepo.insert(user);
            return  true;
        }
        catch (Exception e){
            return  false;
        }
    }
    public boolean canResend(String username){
        User user=userrepo.findByUsername(username);
        if (user==null){
            return false;
        }
        if(!user.isEnabled()){
            return true;
        }
        return false;
    }

    public boolean otpValidation(OtpRequest req){
        Optional<OptValidation> otp=opTrepo.findById(req.getValidationID());
        if (otp.isEmpty()) return false;
        OptValidation valid=otp.get();
        if(!valid.getOpt().equals(req.getOtp())){
            return false;
        }
        opTrepo.deleteById(valid.getValidationID());
        return true;
    }

    public String setAPIKEY(String username){
        User user=userrepo.findByUsername(username);
        String token=jwtUtils.get_Api_Jwt_token(user);
        user.setApi_key(token);
        userrepo.save(user);
        return token;
    }

    public void saveChanges(User user){
        userrepo.save(user);
    }

    public void EnableUser(String username){
        User user=userrepo.findByUsername(username);
        user.setEnabled(true);
        userrepo.save(user);
    }
}
