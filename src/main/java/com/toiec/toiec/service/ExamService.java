package com.toiec.toiec.service;

import com.toiec.toiec.dto.response.exam.ExamDetailResponse;
import com.toiec.toiec.dto.response.exam.ExamResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExamService {
    List<ExamResponse> findAllExam();
    ExamDetailResponse getExamById(Integer id);

}
