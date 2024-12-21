package com.toiec.toiec.service;

import com.toiec.toiec.dto.request.exam.CreateExam;
import com.toiec.toiec.dto.request.exam.QuestionGroupRequest;
import com.toiec.toiec.dto.response.exam.ExamDetailResponse;
import com.toiec.toiec.dto.response.exam.ExamPartDetailResponse;
import com.toiec.toiec.dto.response.exam.ExamResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface ExamService {
    List<ExamResponse> findAllExam();
    ExamDetailResponse getExamDetailById(Integer id) throws IOException;
    ExamResponse createExam(CreateExam createExam) ;
    Integer addQuestionGroupForExam(Integer examId,Integer questionGroupId,QuestionGroupRequest questionGroupRequest);
    ExamPartDetailResponse getPartOfExamDetailById(Integer id, String part) throws IOException;
}
