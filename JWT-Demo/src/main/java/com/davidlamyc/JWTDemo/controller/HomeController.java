package com.davidlamyc.JWTDemo.controller;

import com.davidlamyc.JWTDemo.model.JwtRequest;
import com.davidlamyc.JWTDemo.model.JwtResponse;
import com.davidlamyc.JWTDemo.model.ValidateJwtRequest;
import com.davidlamyc.JWTDemo.model.ValidateJwtResponse;
import com.davidlamyc.JWTDemo.service.UserService;
import com.davidlamyc.JWTDemo.utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        return "Welcome home";
    }

    @PostMapping("/authenticate")
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        final UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUsername());

        final String token = jwtUtility.generateToken(userDetails);

        return new JwtResponse(token);
    }

    // Note to self: never code like this
    @PostMapping("/validate")
    public ValidateJwtResponse validate(@RequestBody ValidateJwtRequest jwtTokenRequest) {
        String token = null;
        String userName = null;

        if(jwtTokenRequest.getJwtToken().startsWith("Bearer ")) {
            token = jwtTokenRequest.getJwtToken().substring(7);
            userName = jwtUtility.getUsernameFromToken(token);
        }

        if(null != userName) {
            UserDetails userDetails
                    = userService.loadUserByUsername(userName);

            ValidateJwtResponse validateJwtResponse = new ValidateJwtResponse();
            validateJwtResponse.setValid(true);

            return validateJwtResponse;
        }

        ValidateJwtResponse validateJwtResponse = new ValidateJwtResponse();
        validateJwtResponse.setValid(false);

        return validateJwtResponse;
    }
}
