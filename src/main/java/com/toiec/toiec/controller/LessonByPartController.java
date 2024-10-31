package com.toiec.toiec.controller;

import com.toiec.toiec.dto.ResponseGeneral;
import com.toiec.toiec.dto.request.auths.LoginRequest;
import com.toiec.toiec.dto.response.auths.LoginResponse;
import com.toiec.toiec.service.AuthService;
import com.toiec.toiec.service.LessonByPartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.headers.HeadersSecurityMarker;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/lessonbypart")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class LessonByPartController
{
    private final LessonByPartService lessonByPartService;
    @GetMapping("")
    public ResponseEntity<?> getListQuestionByPartPage(
            @Param("count") int count,
            @Param("part") String part) throws IOException {

        return new ResponseEntity<>(ResponseGeneral.of(200,"success",lessonByPartService.getQuestionsByType(part,count)), HttpStatus.OK);

    }
//    public ResponseEntity<?> historyPartOfUser(
//            @Param("count") int count,
//            @Param("part") String part)
//    {
//
//        return new ResponseEntity<>(ResponseGeneral.of(200,"success",lessonByPartService.historyPartOfUser(part,count)), HttpStatus.OK);
//
//    }
}
