package com.projects.booksphere.auth.service;

import com.projects.booksphere.auth.model.AuthenticationRequest;
import com.projects.booksphere.auth.model.AuthenticationResponse;
import com.projects.booksphere.auth.model.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    String refreshToken(String jwt);
}