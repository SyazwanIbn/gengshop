package com.project.gengshop.service;

import com.project.gengshop.dto.LoginRequestDto;
import com.project.gengshop.dto.RegisterRequestDto;
import com.project.gengshop.model.User;
import com.project.gengshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public void registerNewUser(RegisterRequestDto registerRequestDto) {
        //check if email exist
        if (userRepository.findByEmail(registerRequestDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        //create new user
        User newUser = new User();
        newUser.setUsername(registerRequestDto.getUsername());
        newUser.setEmail(registerRequestDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));

        //save user
        userRepository.save(newUser);
    }

    public String loginUser(LoginRequestDto loginRequestDto) {
        // authenticate user dengan data input dari controller
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()
                )
        );

        // Setkan authentication dalam security context untuk request sekarang
        //(Ni sebenarnya optional untuk dptbalik token je, tapi bagus untuk ada)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Dapatkan userDetails dari object Authentication
        // User ni adalah objek yang kita dah implement UserDetails interface
       UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 4. Generate token guna jwtService
        String jwtToken = jwtService.generateToken(userDetails);

        // 5. Pulangkan token
        return jwtToken;
    }
}
