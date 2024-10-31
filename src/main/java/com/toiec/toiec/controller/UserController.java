package com.toiec.toiec.controller;


import com.toiec.toiec.dto.ResponseGeneral;
import com.toiec.toiec.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/v1/accounts")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/infor")
    public ResponseEntity<?> findInforAccountLogin(Principal principal)
    {
        String username = principal.getName();
        ResponseGeneral<?> responseGeneral= ResponseGeneral.of(200,
                "success",userService.findInforByUsername(username));
        return new ResponseEntity<>(responseGeneral, HttpStatus.OK);
    }
}