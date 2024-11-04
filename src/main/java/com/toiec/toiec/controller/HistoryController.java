package com.toiec.toiec.controller;

import com.toiec.toiec.dto.ResponseGeneral;
import com.toiec.toiec.dto.request.auths.LoginRequest;
import com.toiec.toiec.dto.response.auths.LoginResponse;
import com.toiec.toiec.dto.response.history.HistoryResponse;
import com.toiec.toiec.entity.Histories;
import com.toiec.toiec.service.HistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/history")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")

public class HistoryController {

    private final HistoryService historyService;

    @GetMapping("/last")
    public ResponseEntity<?> getHistoryLastOfUser(
        Principal principal,
        @RequestParam(value = "type", required = true) String type
    )
    {
        ResponseGeneral<HistoryResponse> responseGeneral = ResponseGeneral.of(
                200,
                "success",
                historyService.getLastNHistoryByUsernameAndType(principal.getName(),type)
        );
        return new ResponseEntity<>(responseGeneral, HttpStatus.OK);
    }
}

