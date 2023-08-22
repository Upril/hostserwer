package com.serwertetowy.auth;

import com.serwertetowy.config.JwtService;
import com.serwertetowy.entities.User;
import com.serwertetowy.exceptions.FailedToAuthenticateException;
import com.serwertetowy.exceptions.UserDeletedException;
import com.serwertetowy.exceptions.UserNotFoundException;
import com.serwertetowy.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private ResourceLoader resourceLoader;
    public AuthenticationController.AuthenticationResponse register(AuthenticationController.RegisterRequest request) throws IOException {
        var user = new User(
                request.firstname(),
                request.lastname(),
                request.email(),
                passwordEncoder.encode(request.password()),
                request.role()
        );
        user.setImageData(resourceLoader.getResource("classpath:/images/defalt.jpg").getContentAsByteArray());
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationController.AuthenticationResponse(jwtToken);
    }

    public AuthenticationController.AuthenticationResponse authenticate(AuthenticationController.AuthenticationRequest request) {
        if(!userRepository.existsByEmail(request.email())) throw new UserNotFoundException();
        if(userRepository.findByEmail(request.email()).getDeleted()) throw new UserDeletedException();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(),request.password()));
        var user = userRepository.findById(userRepository.findByEmail(request.email()).getId()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationController.AuthenticationResponse(jwtToken);
    }
    public static String getIdentity(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        String authIdentity = null;
        if (auth != null){
            authIdentity = auth.getName();
        }
        else {
            throw new FailedToAuthenticateException();
        }
        return authIdentity;
    }
}
