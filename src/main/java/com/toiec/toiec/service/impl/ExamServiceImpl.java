package com.toiec.toiec.service.impl;

import com.toiec.toiec.dto.request.exam.CreateExam;
import com.toiec.toiec.dto.response.exam.ExamDetailResponse;
import com.toiec.toiec.dto.response.exam.ExamResponse;
import com.toiec.toiec.dto.response.exam.PartResponse;
import com.toiec.toiec.entity.Exam;
import com.toiec.toiec.repository.ExamRepository;
import com.toiec.toiec.service.ExamService;
import com.toiec.toiec.utils.JsonUtils;
import com.toiec.toiec.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
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
    public ExamResponse createExam(CreateExam createExam)
    {
        Exam exam = new Exam();
        exam.setExamName(createExam.getExamName());
        exam.setStatus("0");
        exam=examRepository.save(exam);
        return MapperUtils.toDTO(exam,ExamResponse.class);
    }
    @Override
    public ExamDetailResponse getExamDetailById(Integer examId) throws IOException {
        ExamDetailResponse examDetailResponse = new ExamDetailResponse();
        List< Object[]> examDetailObjects=examRepository.findExamDetails(examId);
        if(!examDetailObjects.isEmpty()){
            examDetailResponse.setIdExam(examId);
            if(examDetailObjects.get(0)[1]!=null)examDetailResponse.setExamName((String) examDetailObjects.get(0)[1]);
        }
        List<PartResponse> partResponses = new ArrayList<>();
        for(Object[] examDetailObject : examDetailObjects){
            partResponses.add(JsonUtils.fromJson((String)examDetailObject[2],PartResponse.class));
        }
        examDetailResponse.setPartResponseList(partResponses);
        return examDetailResponse;
    }
}
