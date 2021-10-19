package com.davidlamyc.JWTDemo.service;

import com.davidlamyc.JWTDemo.model.RestUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername (String userName) throws UsernameNotFoundException {
        // logic to get the user from the db/service
        // just create dummy user object in this case
        // return new User("admin", "password", new ArrayList<>());

        RestUser restUser =
                restTemplate.getForObject(
                        "http://localhost:9002/users/" + userName, RestUser.class);

        return new User(restUser.getUsername(), restUser.getPassword(), new ArrayList<>());
    }
}
