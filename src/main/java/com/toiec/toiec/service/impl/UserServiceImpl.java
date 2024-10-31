package com.toiec.toiec.service.impl;

import com.toiec.toiec.config.security.JwtUtilities;
import com.toiec.toiec.dto.response.account.InforAccountLoginResponse;
import com.toiec.toiec.exception.user.UsernameNotFoundException;
import com.toiec.toiec.repository.UserRepository;
import com.toiec.toiec.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtUtilities jwtUtilities;


    @Override
    public InforAccountLoginResponse findInforByUsername(String username)
    {
        List<Object[]> results= userRepository.findInforByUsernameWithRoles(username);
        if(results.isEmpty()) throw new UsernameNotFoundException();
        InforAccountLoginResponse inforAccountLoginResponse = new InforAccountLoginResponse(results);
        return inforAccountLoginResponse ;
    }
}