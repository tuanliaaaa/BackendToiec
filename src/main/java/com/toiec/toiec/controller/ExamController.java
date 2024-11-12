package com.toiec.toiec.controller;

import com.toiec.toiec.dto.ResponseGeneral;
import com.toiec.toiec.dto.request.auths.LoginRequest;
import com.toiec.toiec.dto.response.auths.LoginResponse;
import com.toiec.toiec.service.AuthService;
import com.toiec.toiec.service.ExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/exams")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class ExamController {
    private final ExamService examService;

    @GetMapping("")
    public ResponseEntity<?> getAllExams()
    {

        ResponseGeneral<?> responseGeneral=ResponseGeneral.ofSuccess(
                examService.findAllExam());
        return new ResponseEntity<>(responseGeneral, HttpStatus.OK);
    }
    @GetMapping("{examID}")
    public ResponseEntity<?> getExamById(@PathVariable("examID") Integer examID){
        ResponseGeneral<?> responseGeneral=ResponseGeneral.ofSuccess(
                examService.getExamById(examID)
        );
        return new ResponseEntity<>(responseGeneral, HttpStatus.OK);
    }
}
