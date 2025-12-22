package com.project.gengshop.service;

import com.project.gengshop.config.UserPrincipal;
import com.project.gengshop.model.User;
import com.project.gengshop.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final  UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //cari user dalam bd
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException(
                        "Email not found with email: " + email));
        return new UserPrincipal(user);
    }
}
