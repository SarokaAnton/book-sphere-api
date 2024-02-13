package com.projects.booksphere.auth.controller;

import com.projects.booksphere.auth.model.AuthenticationRequest;
import com.projects.booksphere.auth.model.AuthenticationResponse;
import com.projects.booksphere.auth.model.RegisterRequest;
import com.projects.booksphere.auth.service.AuthenticationService;
import com.projects.booksphere.utils.exceptionhandler.exceptions.InvalidTokenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.projects.booksphere.utils.exceptionhandler.ExceptionMessages.REQUEST_SHOULD_CONTAIN_TOKEN;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    public static final String BEARER_TOKEN_TYPE = "Bearer ";

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Void> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return generateResponseWithRefreshedToken(request, response);
    }

    private ResponseEntity<Void> generateResponseWithRefreshedToken(HttpServletRequest request, HttpServletResponse response) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER_NAME);

        if (bearerToken == null || !bearerToken.startsWith(BEARER_TOKEN_TYPE)) {
            throw new InvalidTokenException(REQUEST_SHOULD_CONTAIN_TOKEN);
        }

        String jwt = bearerToken.substring(7);
        String refreshedToken = authenticationService.refreshToken(jwt);
        response.setHeader(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN_TYPE + refreshedToken);
        return ResponseEntity.noContent().build();
    }
}