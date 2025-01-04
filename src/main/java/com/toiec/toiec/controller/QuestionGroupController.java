package com.toiec.toiec.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toiec.toiec.dto.ResponseGeneral;
import com.toiec.toiec.dto.request.auths.LoginRequest;
import com.toiec.toiec.dto.request.questiongroups.QuestionGroupRequest;
import com.toiec.toiec.dto.response.auth.LoginResponse;
import com.toiec.toiec.dto.response.questiongroup.QuestionGroupResponse;
import com.toiec.toiec.service.AuthService;
import com.toiec.toiec.service.QuestionGroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/questiongroups")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class QuestionGroupController {
    private final QuestionGroupService questionGroupService;

    @GetMapping("/search")
    public ResponseEntity<?> login(
        @RequestParam(value = "value",required = true) String value,
        @RequestParam(value = "type",required = false ) String type,
        @RequestParam(value= "size",defaultValue = "4") Integer size,
        @RequestParam(value= "page",defaultValue = "0") Integer page
    ) throws IOException {

        ResponseGeneral<List<QuestionGroupResponse>> responseGeneral=ResponseGeneral.ofSuccess(
                questionGroupService.searchQuestionGroup(value,type,page,size));
        return new ResponseEntity<>(responseGeneral, HttpStatus.OK);
    }
    @GetMapping("/{questionGroupId}")
    public ResponseEntity<?> getQuestionGroupByID(
        @PathVariable("questionGroupId") Integer questionGroupId
    ) throws IOException {

        ResponseGeneral<QuestionGroupResponse> responseGeneral=ResponseGeneral.ofSuccess(
                questionGroupService.getQuestionGroupByID(questionGroupId));
        return new ResponseEntity<>(responseGeneral, HttpStatus.OK);
    }

    @PostMapping(value = "questionGroups", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addQuestionGroup(
            @RequestPart("questionGroup") String questionGroupData,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        QuestionGroupRequest questionGroup = objectMapper.readValue(questionGroupData, QuestionGroupRequest.class);

        // Gửi dữ liệu tới service để xử lý
        questionGroupService.createQuestionGroup(questionGroup, files);

        return new ResponseEntity<>(
                ResponseGeneral.ofCreated("questionGroup", "Created successfully"),
                HttpStatus.CREATED
        );
    }

}
