package com.toiec.toiec.service;

import com.toiec.toiec.dto.request.auths.LoginRequest;
import com.toiec.toiec.dto.response.auth.LoginResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    LoginResponse login(LoginRequest loginRequest) ;
//    InforAccountLoginResponse findInforByUsername(String username);
//    LoginResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
