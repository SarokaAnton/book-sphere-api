package com.projects.booksphere.auth.service;

import com.projects.booksphere.auth.config.service.JwtService;
import com.projects.booksphere.auth.model.AuthenticationRequest;
import com.projects.booksphere.auth.model.AuthenticationResponse;
import com.projects.booksphere.auth.model.RegisterRequest;
import com.projects.booksphere.user.model.Role;
import com.projects.booksphere.user.model.User;
import com.projects.booksphere.user.repository.UserRepository;
import com.projects.booksphere.utils.exceptionhandler.exceptions.ElementAlreadyExistsException;
import com.projects.booksphere.utils.exceptionhandler.exceptions.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.projects.booksphere.utils.exceptionhandler.ExceptionMessages.*;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceDefault implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .nickname(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .secondName(request.getSecondName())
                .role(Role.USER)
                .build();

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ElementAlreadyExistsException(String.format(USER_ALREADY_EXIST, user.getEmail()));
        }

        userRepository.save(user);

        return buildAuthenticationResponse(user);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword())
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new UsernameNotFoundException(USER_NOT_FOUND)
        );

        return buildAuthenticationResponse(user);
    }

    @Override
    public String refreshToken(String jwt) {
        String userEmail = jwtService.extractUsername(jwt);
        if (userEmail == null) {
            throw new InvalidTokenException(INVALID_EMAIL_IN_TOKEN);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
        if (!jwtService.isTokenValid(jwt, userDetails)) {
            throw new InvalidTokenException(INVALID_REFRESH_TOKEN);
        }

        return jwtService.generateAccessToken(userDetails);
    }

    private AuthenticationResponse buildAuthenticationResponse(User user) {
        var jwtAccessToken = jwtService.generateAccessToken(user);
        var jwtRefreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .id(user.getId())
                .build();
    }
}