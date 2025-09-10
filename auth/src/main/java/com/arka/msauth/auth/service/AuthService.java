package com.arka.msauth.auth.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.arka.msauth.auth.dto.LoginRequest;
import com.arka.msauth.auth.dto.RegisterRequest;
import com.arka.msauth.auth.model.User;
import com.arka.msauth.auth.repository.UserRepository;
import com.arka.msauth.auth.util.JwtUtil;

import reactor.core.publisher.Mono;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Mono<User> register(RegisterRequest request){
        return userRepository.findByUsername(request.getUsername())
                .flatMap(existing -> Mono.<User>error(new RuntimeException("Username already exists")))
                .switchIfEmpty(Mono.defer(() -> userRepository.save(
                    new User(null, request.getClientId(), request.getUsername(), passwordEncoder.encode(request.getPassword()))
                )));
    }

    public Mono<String> login(LoginRequest request){
        return userRepository.findByUsername(request.getUsername())
                .flatMap(user -> {
                    if(passwordEncoder.matches(request.getPassword(), user.getPasswordHash())){
                        return Mono.just(JwtUtil.generateToken(user.getUsername(), user.getClientId()));
                    } else {
                        return Mono.error(new RuntimeException("Invalid credentials"));
                    }
                })
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")));
    }
}
