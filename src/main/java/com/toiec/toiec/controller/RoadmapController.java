package com.toiec.toiec.controller;

import com.toiec.toiec.dto.ResponseGeneral;
import com.toiec.toiec.service.RoadmapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/roadmaps")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class RoadmapController {
    private final RoadmapService roadmapService;
    @GetMapping("")
    public ResponseEntity<?> findAllRoadMap() {
        return new ResponseEntity<>(ResponseGeneral.of(200,"success",roadmapService.findAllRoadMap()), HttpStatus.OK);
    }
    @GetMapping("{idRoadMap}")
    public ResponseEntity<?> getRoadMap(
            @PathVariable int idRoadMap
    ) throws IOException {
        return new ResponseEntity<>(ResponseGeneral.of(200,"success",roadmapService.findLessonesByRoodmapId(idRoadMap)), HttpStatus.OK);
    }
    @GetMapping("grammas/{idGrammar}")
    public ResponseEntity<?> getidGrammarById(
            @PathVariable int idGrammar
    ) throws IOException {
        return new ResponseEntity<>(ResponseGeneral.of(200,"success",roadmapService.getGrammarById(idGrammar)), HttpStatus.OK);
    }
}
