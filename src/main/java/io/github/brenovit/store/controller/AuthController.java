package io.github.brenovit.store.controller;

import static org.springframework.http.ResponseEntity.ok;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.brenovit.store.config.JwtTokenProvider;
import io.github.brenovit.store.dto.AuthBody;
import io.github.brenovit.store.dto.GenericResponse;
import io.github.brenovit.store.dto.LoginResponse;
import io.github.brenovit.store.model.User;
import io.github.brenovit.store.repository.UserRepository;
import io.github.brenovit.store.service.CustomUserDetailsService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository users;

    @Autowired
    private CustomUserDetailsService userService;

    @SuppressWarnings("rawtypes")
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthBody data) {
        try {
            String username = data.getEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            String token = jwtTokenProvider.createToken(username, this.users.findByEmail(username).getRoles());
            return ok(new LoginResponse(username, token));
        } catch (AuthenticationException e) {
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericResponse(HttpStatus.UNAUTHORIZED, "Invalid email/password supplied"));
        }
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody User user) {
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericResponse(HttpStatus.UNAUTHORIZED, "User with username: " + user.getEmail() + " already exists"));
        }
        userService.saveUser(user);
        return ok(new GenericResponse(HttpStatus.CREATED, "User registered successfully"));
    }
}
