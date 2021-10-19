package com.davidlamyc.userservice.controller;

import com.davidlamyc.userservice.entity.JwtValidationRequest;
import com.davidlamyc.userservice.entity.JwtValidationResponse;
import com.davidlamyc.userservice.entity.User;
import com.davidlamyc.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/")
    public User saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping("/{username}")
    public User getUserByUsername(@PathVariable("username") String username) {
        return userService.getByUsername(username);
    }

    // note to self - never code like this
    @GetMapping("/testendpoint")
    public boolean getFakeString(@RequestHeader("Authorization") String token) {
        JwtValidationRequest jwtValidationRequest = new JwtValidationRequest();
        jwtValidationRequest.setJwtToken(token);

        JwtValidationResponse jwtValidationResponse =
                restTemplate.postForObject(
                        "http://localhost:9001/validate",
                        jwtValidationRequest,
                        JwtValidationResponse.class);

        return jwtValidationResponse.isValid();
    }
}
