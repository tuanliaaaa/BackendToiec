package com.toiec.toiec.controller;


import com.toiec.toiec.dto.ResponseGeneral;
import com.toiec.toiec.dto.request.user.EditUserRequest;
import com.toiec.toiec.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
        if (principal == null) {
            return new ResponseEntity<>("ResponseEntity", HttpStatus.UNAUTHORIZED);
        }
        String username = principal.getName();
        ResponseGeneral<?> responseGeneral= ResponseGeneral.of(200,
                "success",userService.findInforByUsername(username));
        return new ResponseEntity<>(responseGeneral, HttpStatus.OK);
    }

    @PatchMapping("/infor")
    public ResponseEntity<?> editInforUser(
            Principal principal,
            @RequestBody EditUserRequest editUserRequest
            )
    {
        String username = principal.getName();
        ResponseGeneral<?> responseGeneral= ResponseGeneral.of(200,
                "success",userService.editInforUser(editUserRequest,username));
        return new ResponseEntity<>(responseGeneral, HttpStatus.OK);
    }

}