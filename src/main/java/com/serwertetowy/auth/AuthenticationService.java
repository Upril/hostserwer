package com.serwertetowy.auth;

import com.serwertetowy.config.JwtService;
import com.serwertetowy.entities.Role;
import com.serwertetowy.entities.User;
import com.serwertetowy.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationController.AuthenticationResponse register(AuthenticationController.RegisterRequest request) {
        var user = new User(
                request.firstname(),
                request.lastname(),
                request.email(),
                passwordEncoder.encode(request.password()),
                Role.USER
        );
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationController.AuthenticationResponse(jwtToken);
    }

    public AuthenticationController.AuthenticationResponse authenticate(AuthenticationController.AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(),request.password()));
        var user = userRepository.findById(userRepository.findByEmail(request.email()).getId()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationController.AuthenticationResponse(jwtToken);
    }
}
