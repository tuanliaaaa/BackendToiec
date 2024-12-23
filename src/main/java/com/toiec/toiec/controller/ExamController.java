package com.toiec.toiec.controller;

import com.toiec.toiec.dto.ResponseGeneral;
import com.toiec.toiec.dto.request.exam.CreateExam;
import com.toiec.toiec.dto.request.exam.QuestionGroupRequest;
import com.toiec.toiec.service.ExamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

    @PostMapping("")
    public ResponseEntity<?> createExam(@RequestBody CreateExam createExam)
    {

        ResponseGeneral<?> responseGeneral=ResponseGeneral.ofCreated(
                "exam",
                examService.createExam(createExam));
        return new ResponseEntity<>(responseGeneral, HttpStatus.CREATED);
    }

    @GetMapping("{examID}")
    public ResponseEntity<?> getExamDetailById(@PathVariable("examID") Integer examID) throws IOException {
        ResponseGeneral<?> responseGeneral=ResponseGeneral.ofSuccess(
                examService.getExamDetailById(examID)
        );
        return new ResponseEntity<>(responseGeneral, HttpStatus.OK);
    }


    @GetMapping("{examID}/part/{partID}")
    public ResponseEntity<?> getPartOfExamDetailById(
            @PathVariable("examID") Integer examID,
            @PathVariable("partID")  Integer partID
            ) throws IOException {
        ResponseGeneral<?> responseGeneral=ResponseGeneral.ofSuccess(
                examService.getPartOfExamDetailById(examID,"part"+String.valueOf(partID))
        );
        return new ResponseEntity<>(responseGeneral, HttpStatus.OK);
    }



    @GetMapping("{examID}/question/{questionGroupId}")
    public ResponseEntity<?> addQuestionForExam (
            @PathVariable("examId") Integer examId,
            @PathVariable("questionGroupId") Integer questionGroupId,
            @RequestBody QuestionGroupRequest questionGroupRequest
    ){
        ResponseGeneral<?> responseGeneral=ResponseGeneral.ofSuccess(
                examService.addQuestionGroupForExam(examId,questionGroupId,questionGroupRequest)
        );
        return new ResponseEntity<>(responseGeneral, HttpStatus.OK);
    }




}
