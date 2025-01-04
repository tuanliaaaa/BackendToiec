package com.toiec.toiec.service.impl;

import com.toiec.toiec.config.security.JwtUtilities;
import com.toiec.toiec.dto.request.auths.ChangePasswordRequest;
import com.toiec.toiec.dto.request.auths.LoginRequest;
import com.toiec.toiec.dto.request.auths.SignupRequest;
import com.toiec.toiec.dto.response.auth.ChangePasswordResponse;
import com.toiec.toiec.dto.response.auth.LoginResponse;
import com.toiec.toiec.dto.response.auth.SignupResponse;
import com.toiec.toiec.entity.RefreshToken;
import com.toiec.toiec.entity.Role;
import com.toiec.toiec.entity.User;
import com.toiec.toiec.entity.UserRole;
import com.toiec.toiec.exception.user.UsernameNotFoundException;
import com.toiec.toiec.repository.RefreshTokenRepository;
import com.toiec.toiec.repository.RoleRepository;
import com.toiec.toiec.repository.UserRepository;
import com.toiec.toiec.repository.UserRoleRepository;
import com.toiec.toiec.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service

@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtilities jwtUtilities;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

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

    @Override
    public SignupResponse signup(SignupRequest signupRequest)
    {
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        String hashedPassword = new BCryptPasswordEncoder().encode(signupRequest.getPassword());
        user.setPassword(hashedPassword);
        user.setStatus("active");
        user.setName(signupRequest.getFullname());
        user = userRepository.save(user);

        Role role = roleRepository.getById(2);
        UserRole userRole = new UserRole();
        userRole.setRole(role);
        userRole.setUser(user);
        userRoleRepository.save(userRole);
        String token = jwtUtilities.generateToken(user.getUsername());
        String refreshToken= jwtUtilities.generateRefreshToken(user.getUsername());
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setRefreshToken(refreshToken);
        refreshTokenEntity.setUser(user);
        refreshTokenEntity.setExpiration(LocalDateTime.now());

        //Save refreshToken to Database
        refreshTokenRepository.save(refreshTokenEntity);

        return new SignupResponse(token,refreshToken);
    }


    @Override
    public ChangePasswordResponse changePasswordRequest(ChangePasswordRequest changePasswordRequest, String username)
    {
        User user = userRepository.findByUsername(username).orElseThrow(UsernameNotFoundException::new);
        String salt = user.getPassword().substring(0,29);
        String hashedPassword = BCrypt.hashpw(changePasswordRequest.getOldPassword(), salt);
        if (!hashedPassword.equals(user.getPassword())) {
            throw new UsernameNotFoundException();
        }
        user.setPassword(new BCryptPasswordEncoder().encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
        return new ChangePasswordResponse();
    }


}
