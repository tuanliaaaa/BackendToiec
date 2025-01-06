package com.toiec.toiec.controller;

import com.toiec.toiec.dto.ResponseGeneral;
import com.toiec.toiec.dto.request.history.HistoryRequest;
import com.toiec.toiec.dto.request.history.exam.CreateExamUser;
import com.toiec.toiec.dto.request.history.lessonbypart.HistoryLessonByPartRequest;
import com.toiec.toiec.dto.request.history.vocabulary.HistoryTopicRequest;
import com.toiec.toiec.dto.response.history.HistoryResponse;
import com.toiec.toiec.dto.response.history.learningpath.HistoryLearningPathResponse;
import com.toiec.toiec.dto.response.history.lessonbypart.HistoryDetailLessonByPartResponse;
//import com.toiec.toiec.dto.response.history.vocabulary.HistoryVocabularyResponse;
import com.toiec.toiec.dto.response.history.vocabulary.HistoryVocabularyResponse;
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

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/histories")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping("lessonbypart")
    public ResponseEntity<?> getHistoryLessonByPartOfUser(
        Principal principal,
        @RequestParam(value = "type", required = false) String type,
        @RequestParam(value = "size", required = true) Integer size,
        @RequestParam(value = "page", required = true) Integer page

    )
    {
        ResponseGeneral<List<HistoryResponse>> responseGeneral = ResponseGeneral.ofSuccess(
                historyService.findHistoryOfUsernameByType(principal.getName(),type,page,size)
        );
        return new ResponseEntity<>(responseGeneral, HttpStatus.OK);
    }

    @PostMapping("lessonbypart")
    public ResponseEntity<?> createHistoryLessonByPartOfUser(
            Principal principal,
            @RequestBody HistoryLessonByPartRequest historyLessonByPartRequest
            )
    {
        ResponseGeneral<?> responseGeneral = ResponseGeneral.ofCreated(
                "history learning path",
                historyService.createHistoryLessonByPartOfUsernameByType(principal.getName(),historyLessonByPartRequest)
        );
        return new ResponseEntity<>(responseGeneral, HttpStatus.CREATED);
    }
    @PostMapping("exams/{examId}")
    public ResponseEntity<?> createHistoryExamOfUser(
            Principal principal,
            @PathVariable("examId") Integer examId,
            @RequestBody CreateExamUser createExamUser
    ) throws IOException {
        ResponseGeneral<?> responseGeneral = ResponseGeneral.ofCreated(
                "history learning path",
                historyService.createHistoryExamOfUser(principal.getName(),examId,createExamUser)
        );
        return new ResponseEntity<>(responseGeneral, HttpStatus.CREATED);
    }


    @GetMapping("lessonbypart/{idHistory}")
    public ResponseEntity<?> getHistoryById(
            @PathVariable("idHistory") Integer idHistory,
            Principal principal
    ) throws IOException {
        ResponseGeneral<HistoryDetailLessonByPartResponse> responseGeneral = ResponseGeneral.ofSuccess(
                historyService.getHistoryPartByIdAndUsername(idHistory,principal.getName())
        );
        return new ResponseEntity<>(responseGeneral, HttpStatus.OK);
    }

    @GetMapping("exam")
    public ResponseEntity<?> getHistoryExamOfUser(
            Principal principal,
            @RequestParam(value = "size", required = true) Integer size,
            @RequestParam(value = "page", required = true) Integer page
    )
    {
        ResponseGeneral<List<HistoryResponse>> responseGeneral = ResponseGeneral.ofSuccess(
                historyService.findHistoryExamOfUsername(principal.getName(),page,size)
        );
        return new ResponseEntity<>(responseGeneral, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> CreateHistoryOfUser(
            Principal principal,
            @RequestBody @Valid HistoryRequest historyRequest
    ) throws IOException {
        ResponseGeneral<HistoryResponse> responseGeneral = ResponseGeneral.ofCreated(
                "history",
                historyService.createHistoryOfUser(principal.getName(),historyRequest)
        );
        return new ResponseEntity<>(responseGeneral, HttpStatus.OK);
    }

    @PostMapping("vocabularies")
    public ResponseEntity<?> createWordHistoryOfUser(
            Principal principal,
            @RequestBody @Valid HistoryTopicRequest historyRequest
    ){
        ResponseGeneral<HistoryVocabularyResponse> responseGeneral =
                ResponseGeneral.ofCreated(
                    "history",
                    historyService.createWordHistoryOfUser(principal.getName(),historyRequest)
        );
        return new ResponseEntity<>(responseGeneral, HttpStatus.OK);
    }

    @GetMapping("vocabularies")
    public ResponseEntity<?> getHistoryWordOfUser(
            Principal principal,
            @RequestParam(value = "size", required = true) Integer size,
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "idLesson",required = false) Integer idLesson

    )
    {
        ResponseGeneral<List<HistoryVocabularyResponse>> responseGeneral = ResponseGeneral.ofSuccess(
                historyService.findHistoryWordOfUsernameByType(principal.getName(),page,size,idLesson)
        );
        return new ResponseEntity<>(responseGeneral, HttpStatus.OK);
    }

    @GetMapping("learningpaths")
    public ResponseEntity<?> getHistoryLearningPathOfUser(
            Principal principal,
            @RequestParam(value = "size", required = true) Integer size,
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "idLesson",required = false) Integer idLesson
    )
    {
        ResponseGeneral<List<HistoryLearningPathResponse>> responseGeneral = ResponseGeneral.ofSuccess(
                historyService.findHistoryLearningPathOfUsernameByType(principal.getName(),page,size,idLesson)
        );
        return new ResponseEntity<>(responseGeneral, HttpStatus.OK);
    }


}

