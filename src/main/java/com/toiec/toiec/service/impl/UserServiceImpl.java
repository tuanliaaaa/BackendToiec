package com.toiec.toiec.service.impl;

import com.toiec.toiec.config.security.JwtUtilities;
import com.toiec.toiec.dto.request.user.EditUserRequest;
import com.toiec.toiec.dto.response.account.EditUserResponse;
import com.toiec.toiec.dto.response.account.InforAccountLoginResponse;
import com.toiec.toiec.entity.User;
import com.toiec.toiec.exception.user.UsernameNotFoundException;
import com.toiec.toiec.repository.UserRepository;
import com.toiec.toiec.service.UserService;
import com.toiec.toiec.utils.JsonUtils;
import com.toiec.toiec.utils.UpdateUtils;
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

    @Override
    public EditUserResponse editInforUser(EditUserRequest editUserRequest, String username)
    {
        User user = userRepository.findByUsername(username).orElseThrow(
                UsernameNotFoundException::new
        );
        UpdateUtils.updateEntityFromDTO( user, editUserRequest );
        userRepository.save(user);
        return new EditUserResponse(username);
    }
}