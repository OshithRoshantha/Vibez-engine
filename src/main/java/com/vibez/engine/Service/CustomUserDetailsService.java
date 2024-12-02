package com.vibez.engine.Service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.vibez.engine.Model.User;
import com.vibez.engine.Repository.UserRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
        public UserDetails loadUserByUsername(String email){
        User user = userRepo.findByEmail(email);
        return new org.springframework.security.core.userdetails.User(
            user.getUserName(), 
            user.getPassword(), 
            Collections.emptyList());
    }
    
}
