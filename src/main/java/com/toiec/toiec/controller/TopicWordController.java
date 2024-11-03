package com.toiec.toiec.controller;

import com.toiec.toiec.dto.request.topicwords.CreateTopicWordRequest;
import com.toiec.toiec.dto.request.topicwords.CreateWordRequest;
import com.toiec.toiec.dto.response.topicwords.TopicWordResponse;
import com.toiec.toiec.dto.response.topicwords.WordResponse;
import com.toiec.toiec.service.TopicWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/topicwords")
@RequiredArgsConstructor
public class TopicWordController {

    private final TopicWordService topicWordService;

    @GetMapping
    public ResponseEntity<?> getAllTopicWords() {
        List<TopicWordResponse> topicWords = topicWordService.getAllTopicWords();
        return ResponseEntity.ok(topicWords);
    }
//
//    @PostMapping
//    public ResponseEntity<?> addTopicWord(@RequestBody CreateTopicWordRequest request) {
//        TopicWordResponse response = topicWordService.addTopicWord(request);
//        return ResponseEntity.status(201).body(response);
//    }
//
//    @GetMapping("/{topicId}/newwords")
//    public ResponseEntity<?> getWordsByTopicId(@PathVariable int topicId) {
//        List<WordResponse> words = topicWordService.getWordsByTopicId(topicId);
//        return ResponseEntity.ok(words);
//    }
//
//    @PostMapping("/{topicId}/newwords")
//    public ResponseEntity<WordResponse> addWordToTopic(@PathVariable int topicId, @RequestBody CreateWordRequest request) {
//        WordResponse response = topicWordService.addWordToTopic(topicId, request);
//        return ResponseEntity.status(201).body(response);
//    }
}
