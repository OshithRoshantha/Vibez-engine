package com.vibez.engine.Service.Implementation;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibez.engine.Model.User;
import com.vibez.engine.Repository.UserRepo;
import com.vibez.engine.Service.UserService;

@Service
public class UserImplement implements UserService {
    @Autowired
    private UserRepo userRepo;

    public User createUser(User newUser) {
        String hashedPassword = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
        newUser.setPassword(hashedPassword);
        return userRepo.save(newUser);
    }

    public boolean authenticateUser(String email, String password) {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            return false;
        }
        return user.getPassword().equals(password);
    }
    
}
