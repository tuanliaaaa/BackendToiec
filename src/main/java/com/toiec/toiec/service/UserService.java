package com.toiec.toiec.service;

import com.toiec.toiec.dto.response.account.InforAccountLoginResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    InforAccountLoginResponse findInforByUsername(String username);
}
