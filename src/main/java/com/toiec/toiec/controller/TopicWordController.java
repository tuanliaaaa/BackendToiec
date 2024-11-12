package com.toiec.toiec.controller;

import com.toiec.toiec.dto.ResponseGeneral;
import com.toiec.toiec.dto.request.topicwords.CreateTopicWordRequest;
import com.toiec.toiec.dto.request.topicwords.CreateWordRequest;
import com.toiec.toiec.dto.response.topicwords.TopicWordResponse;
import com.toiec.toiec.dto.response.topicwords.WordResponse;
import com.toiec.toiec.service.TopicWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/lessonbyskill/vocabulary")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TopicWordController {

    private final TopicWordService topicWordService;

    @GetMapping("topic")
    public ResponseEntity<?> getAllTopicWords() {
        List<TopicWordResponse> topicWords = topicWordService.getAllTopicWords();
        return new ResponseEntity<>(ResponseGeneral.ofSuccess(topicWords), HttpStatus.OK);

    }
//
//    @PostMapping
//    public ResponseEntity<?> addTopicWord(@RequestBody CreateTopicWordRequest request) {
//        TopicWordResponse response = topicWordService.addTopicWord(request);
//        return ResponseEntity.status(201).body(response);
//    }
//
    @GetMapping("topic/{topicId}/newwords")
    public ResponseEntity<?> getWordsByTopicId(@PathVariable int topicId) throws IOException {
        return new ResponseEntity<>(ResponseGeneral.of(200,"success",topicWordService.getWordsByTopicId(topicId)), HttpStatus.OK);
    }

    @GetMapping("topic/{topicId}/exercises")
    public ResponseEntity<?> getExercisesByTopicId(@PathVariable int topicId) throws IOException {
        return new ResponseEntity<>(ResponseGeneral.of(200,"success",topicWordService.getExercisesByTopicId(topicId)), HttpStatus.OK);
    }
//
//    @PostMapping("/{topicId}/newwords")
//    public ResponseEntity<WordResponse> addWordToTopic(@PathVariable int topicId, @RequestBody CreateWordRequest request) {
//        WordResponse response = topicWordService.addWordToTopic(topicId, request);
//        return ResponseEntity.status(201).body(response);
//    }
}
