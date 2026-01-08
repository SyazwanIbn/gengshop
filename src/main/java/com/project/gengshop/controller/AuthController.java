package com.project.gengshop.controller;

import com.project.gengshop.dto.LoginRequestDto;
import com.project.gengshop.dto.RegisterRequestDto;
import com.project.gengshop.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        authService.registerNewUser(registerRequestDto);
        return new ResponseEntity<>("User Created Successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        String token = authService.loginUser(loginRequestDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }


}
