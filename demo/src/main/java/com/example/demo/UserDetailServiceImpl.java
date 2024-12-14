package com.example.demo;

import com.example.demo.Document.User;
import com.example.demo.Repo.Userrepo;
import com.example.demo.exceptions.UserDisabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    Userrepo userrepo;


    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userrepo.findByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException(username);
        }
        if (!user.isEnabled()) {
            throw new UserDisabled();
        }
        return user;
    }
}
