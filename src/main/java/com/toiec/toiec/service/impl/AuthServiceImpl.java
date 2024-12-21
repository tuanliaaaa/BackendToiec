package com.toiec.toiec.service.impl;

import com.toiec.toiec.config.security.JwtUtilities;
import com.toiec.toiec.dto.request.auths.LoginRequest;
import com.toiec.toiec.dto.response.auth.LoginResponse;
import com.toiec.toiec.entity.RefreshToken;
import com.toiec.toiec.entity.User;
import com.toiec.toiec.exception.user.UsernameNotFoundException;
import com.toiec.toiec.repository.RefreshTokenRepository;
import com.toiec.toiec.repository.UserRepository;
import com.toiec.toiec.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service

@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtilities jwtUtilities;
    @Override
    public LoginResponse login(LoginRequest loginRequest)
    {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(UsernameNotFoundException::new);

//         Encrypt the password entered by the user according to the character string stored in the account's database.
        String salt = user.getPassword().substring(0,29);
        String hashedPassword = BCrypt.hashpw(loginRequest.getPassword(), salt);
        if (!hashedPassword.equals(user.getPassword())) {
            throw new UsernameNotFoundException();
        }

//        Check the operational status of account.
//        if(user.getStatus().equals("lock"))throw new AccountIsLockException();

        String token = jwtUtilities.generateToken(user.getUsername());
        String refreshToken= jwtUtilities.generateRefreshToken(user.getUsername());
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setRefreshToken(refreshToken);
        refreshTokenEntity.setUser(user);
        refreshTokenEntity.setExpiration(LocalDateTime.now());
        //Save refreshToken to Database
        refreshTokenRepository.save(refreshTokenEntity);
        return new LoginResponse(token,refreshToken);
    }
}
