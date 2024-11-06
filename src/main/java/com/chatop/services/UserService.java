package com.chatop.services;

import com.chatop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public register findOneUser(){
        return userRepository.findOne();
    }
}
