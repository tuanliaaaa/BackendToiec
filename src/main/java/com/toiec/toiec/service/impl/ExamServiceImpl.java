package com.toiec.toiec.service.impl;

import com.toiec.toiec.dto.response.exam.ExamDetailResponse;
import com.toiec.toiec.dto.response.exam.ExamResponse;
import com.toiec.toiec.entity.Exam;
import com.toiec.toiec.repository.ExamRepository;
import com.toiec.toiec.service.ExamService;
import com.toiec.toiec.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExamServiceImpl implements ExamService {
    private final ExamRepository examRepository;
    @Override
    public List<ExamResponse> findAllExam()
    {
        List<ExamResponse> examsResponse = MapperUtils.toDTOs(examRepository.findAll(),ExamResponse.class);

        return examsResponse;
    }
    @Override
    public ExamDetailResponse getExamById(Integer examId)
    {

        Exam exam = examRepository.findById(examId).orElse(null);
        return MapperUtils.toDTO(exam,ExamDetailResponse.class);
    }
}
