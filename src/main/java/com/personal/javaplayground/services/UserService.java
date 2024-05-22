package com.personal.javaplayground.services;


import com.personal.javaplayground.daos.UserRepository;
import com.personal.javaplayground.exceptions.RecordExistsException;
import com.personal.javaplayground.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User saveUser(User user) {
        if (userRepository.existsById(user.getEmail())) {
            throw new RecordExistsException("User with email " + user.getEmail() + " already exists");
        }
        return userRepository.save(user);
    }

    public User getUser(String email) {
        return userRepository.findById(email).orElse(null);
    }
}