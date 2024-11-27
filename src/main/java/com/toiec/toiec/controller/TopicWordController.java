package com.toiec.toiec.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toiec.toiec.dto.ResponseGeneral;
import com.toiec.toiec.dto.request.topicwords.CreateTopicWordRequest;
import com.toiec.toiec.dto.request.topicwords.CreateWordRequest;
import com.toiec.toiec.dto.response.topicwords.TopicWordResponse;
import com.toiec.toiec.dto.response.topicwords.WordResponse;
import com.toiec.toiec.service.TopicWordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @DeleteMapping("words/{wordId}")
    public ResponseEntity<?> deleteWord(@PathVariable Integer wordId) {
        topicWordService.deleteWordById(wordId);
        return new ResponseEntity<>(
                ResponseGeneral.ofDelete("word"),
                HttpStatus.OK);

    }

    @GetMapping("topic/{topicId}/newwords")
    public ResponseEntity<?> getWordsByTopicId(@PathVariable int topicId) throws IOException {
        return new ResponseEntity<>(ResponseGeneral.of(200,"success",topicWordService.getWordsByTopicId(topicId)), HttpStatus.OK);
    }

    @GetMapping("topic/{topicId}/exercises")
    public ResponseEntity<?> getExercisesByTopicId(@PathVariable int topicId) throws IOException {
        return new ResponseEntity<>(ResponseGeneral.of(200,"success",topicWordService.getExercisesByTopicId(topicId)), HttpStatus.OK);
    }
    @PostMapping(value="topic/{topicId}/words",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addVocabulary(
            @PathVariable Integer topicId,
            @RequestPart("newWord") String newWord,
            @RequestPart(value = "image", required = true) MultipartFile image,
            @RequestPart(value = "audio", required = true) MultipartFile audio) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        CreateWordRequest wordRequest = objectMapper.readValue(newWord, CreateWordRequest.class);

        WordResponse vocabulary = topicWordService.addVocabulary(wordRequest,topicId, image, audio);
        return ResponseEntity.ok(vocabulary);
    }
    @PatchMapping(value="words/{wordId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateVocabulary(
            @PathVariable Integer wordId,
            @RequestPart(value = "newWord",required = false) String newWord,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "audio", required = false) MultipartFile audio) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        CreateWordRequest wordRequest = null;
        if(newWord!=null)  wordRequest = objectMapper.readValue(newWord, CreateWordRequest.class);
         WordResponse vocabulary = topicWordService.editVocabulary(wordRequest,wordId, image, audio);
        return ResponseEntity.ok(vocabulary);
    }
}
