package com.toiec.toiec.controller;

import com.toiec.toiec.dto.ResponseGeneral;
import com.toiec.toiec.dto.request.history.HistoryRequest;
import com.toiec.toiec.dto.request.history.vocabulary.HistoryTopicRequest;
import com.toiec.toiec.dto.response.history.HistoryResponse;
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
    public ResponseEntity<?> getHistoryLastOfUser(
        Principal principal,
        @RequestParam(value = "type", required = true) String type,
        @RequestParam(value = "size", required = true) Integer size,
        @RequestParam(value = "page", required = true) Integer page

    )
    {
        ResponseGeneral<List<HistoryResponse>> responseGeneral = ResponseGeneral.ofSuccess(
                historyService.findHistoryOfUsernameByType(principal.getName(),type,page,size)
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
        ResponseGeneral<HistoryResponse> responseGeneral =
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
            @RequestParam(value = "page", required = true) Integer page

    )
    {
        ResponseGeneral<List<HistoryVocabularyResponse>> responseGeneral = ResponseGeneral.ofSuccess(
                historyService.findHistoryWordOfUsernameByType(principal.getName(),page,size)
        );
        return new ResponseEntity<>(responseGeneral, HttpStatus.OK);
    }

    @GetMapping("learningpaths")
    public ResponseEntity<?> getHistoryLearningPathOfUser(
            Principal principal,
            @RequestParam(value = "size", required = true) Integer size,
            @RequestParam(value = "page", required = true) Integer page

    )
    {
        ResponseGeneral<List<HistoryVocabularyResponse>> responseGeneral = ResponseGeneral.ofSuccess(
                historyService.findHistoryWordOfUsernameByType(principal.getName(),page,size)
        );
        return new ResponseEntity<>(responseGeneral, HttpStatus.OK);
    }


}

