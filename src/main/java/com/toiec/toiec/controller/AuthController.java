package com.toiec.toiec.controller;
import com.toiec.toiec.dto.ResponseGeneral;
import com.toiec.toiec.dto.request.auths.ChangePasswordRequest;
import com.toiec.toiec.dto.request.auths.LoginRequest;
import com.toiec.toiec.dto.request.auths.SignupRequest;
import com.toiec.toiec.dto.response.auth.ChangePasswordResponse;
import com.toiec.toiec.dto.response.auth.LoginResponse;
import com.toiec.toiec.dto.response.auth.SignupResponse;
import com.toiec.toiec.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/auths")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid  @RequestBody LoginRequest loginRequest)
    {
        System.out.println(new BCryptPasswordEncoder().encode("huan"));

        ResponseGeneral<LoginResponse> responseGeneral=ResponseGeneral.ofCreated(
                "token",
                authService.login(loginRequest));
        return new ResponseEntity<>(responseGeneral, HttpStatus.CREATED);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @Valid  @RequestBody SignupRequest signup)
    {
        System.out.println(new BCryptPasswordEncoder().encode("huan"));

        ResponseGeneral<SignupResponse> responseGeneral = ResponseGeneral.ofCreated(
                "token",
                authService.signup(signup));
        return new ResponseEntity<>(responseGeneral, HttpStatus.CREATED);
    }

    @PatchMapping("/changepassword")
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody ChangePasswordRequest changePasswordRequest,
            Principal principal
            ){
        ResponseGeneral<ChangePasswordResponse> responseGeneral=ResponseGeneral.ofSuccess(
                authService.changePasswordRequest(changePasswordRequest, principal.getName()));
        return new ResponseEntity<>(responseGeneral,HttpStatus.OK);
    }




//    @PostMapping("/refreshToken")
//    public ResponseEntity<ResponseGeneral<LoginResponse>> refreshToken(
//            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest)
//    {
//        ResponseGeneral<LoginResponse> responseGeneral=ResponseGeneral.ofCreated(
//                "success", accountService.refreshToken(refreshTokenRequest));
//        return new ResponseEntity<>(responseGeneral, HttpStatus.CREATED);
//    }
}