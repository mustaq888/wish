package com.wishlist.ai.service;

import com.wishlist.ai.domain.User;
import com.wishlist.ai.dto.auth.AuthResponse;
import com.wishlist.ai.dto.auth.LoginRequest;
import com.wishlist.ai.dto.auth.RegisterRequest;
import com.wishlist.ai.exception.BadRequestException;
import com.wishlist.ai.repository.UserRepository;
import com.wishlist.ai.security.AppUserDetails;
import com.wishlist.ai.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email already registered");
        }

        var user = new User();
        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        var savedUser = userRepository.save(user);
        log.info("Registered user {}", savedUser.getEmail());
        return buildResponse(savedUser);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadRequestException("User not found"));
        log.info("User login {}", user.getEmail());
        return buildResponse(user);
    }

    private AuthResponse buildResponse(User user) {
        var userDetails = new AppUserDetails(user);
        return new AuthResponse(
                jwtService.generateToken(userDetails),
                "Bearer",
                user.getId(),
                user.getFullName(),
                user.getEmail()
        );
    }
}
