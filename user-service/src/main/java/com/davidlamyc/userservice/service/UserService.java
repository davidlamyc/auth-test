package com.davidlamyc.userservice.service;

import com.davidlamyc.userservice.entity.User;
import com.davidlamyc.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }
}
