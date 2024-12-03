package com.toiec.toiec.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toiec.toiec.dto.ResponseGeneral;
import com.toiec.toiec.dto.request.roodmap.CreateDay;
import com.toiec.toiec.dto.request.roodmap.CreateGrammar;
import com.toiec.toiec.dto.request.roodmap.UpdateDay;
import com.toiec.toiec.dto.request.roodmap.UpdateGrammar;
import com.toiec.toiec.dto.request.vocabulary.CreateWordRequest;
import com.toiec.toiec.dto.response.roadmap.GrammarResponse;
import com.toiec.toiec.entity.LessonDetail;
import com.toiec.toiec.service.RoadmapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/roadmaps")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class RoadmapController {
    private final RoadmapService roadmapService;
    @PostMapping("days")
    public ResponseEntity<?> createDay(
            @RequestBody CreateDay createDay
    )
    {
        return new ResponseEntity<>(ResponseGeneral.ofCreated("day",roadmapService.createdDay(createDay)),HttpStatus.CREATED);
    }
    @PatchMapping("days/{dayId}")
    public  ResponseEntity<?> updateDay(
            @PathVariable Integer dayId,
            @RequestBody UpdateDay updateDay
    ){
        return new ResponseEntity<>(ResponseGeneral.ofUpdated("day",roadmapService.updateDay(updateDay,dayId)), HttpStatus.OK);
    }
    @DeleteMapping("days/{dayId}")
    public  ResponseEntity<?> deleteDay(
            @PathVariable Integer dayId
    ){
        roadmapService.deleteDay(dayId);
        return new ResponseEntity<>(ResponseGeneral.ofDelete("day"), HttpStatus.NO_CONTENT);
    }
    @GetMapping("/lessons/search")
    public ResponseEntity<?> searchLesson(
            @RequestParam String nameLesson,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    )
    {
        Pageable pageable = PageRequest.of(page, size);
        List<GrammarResponse> lessons = roadmapService.findLessonesByNameLesson(nameLesson, pageable);

        return new ResponseEntity<>(ResponseGeneral.ofSuccess(lessons), HttpStatus.OK);
    }

    @PostMapping ("days/{dayId}/lesson")
    public ResponseEntity<?> updateLessonListFor(
            @RequestBody List<Integer> lsLessonDetail,
            @PathVariable Integer dayId
    ) throws IOException {
        return new ResponseEntity<>(ResponseGeneral.ofSuccess(roadmapService.updateLessonForDay(lsLessonDetail,dayId)), HttpStatus.OK);
    }
    @GetMapping("")
    public ResponseEntity<?> findAllRoadMap() throws IOException {
        return new ResponseEntity<>(ResponseGeneral.ofSuccess(roadmapService.findAllRoadMap()), HttpStatus.OK);
    }
    @GetMapping("{idRoadMap}")
    public ResponseEntity<?> getRoadMap(
            @PathVariable int  idRoadMap
    ) throws IOException {
        return new ResponseEntity<>(ResponseGeneral.of(200,"success",roadmapService.findLessonesByRoodmapId(idRoadMap)), HttpStatus.OK);
    }
    @GetMapping("grammas/{idGrammar}")
    public ResponseEntity<?> getidGrammarById(
            @PathVariable int idGrammar
    ) throws IOException {
        return new ResponseEntity<>(ResponseGeneral.of(200,"success",roadmapService.getGrammarById(idGrammar)), HttpStatus.OK);
    }
    @GetMapping("grammars")
    public ResponseEntity<?> findAllGrammar(
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size
    ) throws IOException {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(ResponseGeneral.ofSuccess(roadmapService.findAllLessonDetail(pageable)), HttpStatus.OK);
    }

    @PostMapping(value = "grammars", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createGrammar(
            @RequestPart("grammar") String createGrammar,
            @RequestPart(value = "file",required = true) MultipartFile file) throws JsonProcessingException {
        {
            ObjectMapper objectMapper = new ObjectMapper();
            CreateGrammar nameLesson = objectMapper.readValue(createGrammar, CreateGrammar.class);

            return new ResponseEntity<>(
                    ResponseGeneral.ofCreated(
                            "grammar",
                            roadmapService.createGrammar(nameLesson, file)
                    )
                    , HttpStatus.CREATED);
        }
    }

    @PatchMapping(value = "grammars/{idGrammar}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateGrammar(
            @PathVariable int idGrammar,
            @RequestPart(value = "grammar",required = false) String updateGrammar,
            @RequestPart(value = "file",required = false) MultipartFile file) throws JsonProcessingException {
        {

            ObjectMapper objectMapper = new ObjectMapper();
            UpdateGrammar grammar=null;
            if(updateGrammar!=null) grammar = objectMapper.readValue(updateGrammar, UpdateGrammar.class);

            return new ResponseEntity<>(
                    ResponseGeneral.ofCreated(
                            "grammar",
                            roadmapService.updateGrammar(idGrammar,grammar, file)
                    )
                    , HttpStatus.CREATED);
        }
    }
}
