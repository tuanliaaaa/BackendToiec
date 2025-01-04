package com.toiec.toiec.service;

import com.toiec.toiec.dto.request.user.EditUserRequest;
import com.toiec.toiec.dto.response.account.EditUserResponse;
import com.toiec.toiec.dto.response.account.InforAccountLoginResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    InforAccountLoginResponse findInforByUsername(String username);
    EditUserResponse editInforUser(EditUserRequest editUserRequest, String username);
}
