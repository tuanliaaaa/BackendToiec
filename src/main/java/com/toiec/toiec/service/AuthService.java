package com.toiec.toiec.service;

import com.toiec.toiec.dto.request.auths.ChangePasswordRequest;
import com.toiec.toiec.dto.request.auths.LoginRequest;
import com.toiec.toiec.dto.request.auths.SignupRequest;
import com.toiec.toiec.dto.response.auth.ChangePasswordResponse;
import com.toiec.toiec.dto.response.auth.LoginResponse;
import com.toiec.toiec.dto.response.auth.SignupResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    LoginResponse login(LoginRequest loginRequest) ;
    ChangePasswordResponse changePasswordRequest(ChangePasswordRequest changePasswordRequest, String username) ;
    SignupResponse signup(SignupRequest signupRequest) ;
//    InforAccountLoginResponse findInforByUsername(String username);
//    LoginResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
