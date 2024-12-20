package com.toiec.toiec.service.impl;

import com.toiec.toiec.dto.request.exam.CreateExam;
import com.toiec.toiec.dto.request.exam.QuestionGroupRequest;
import com.toiec.toiec.dto.response.exam.*;
import com.toiec.toiec.entity.Exam;
import com.toiec.toiec.entity.QuestionGroup;
import com.toiec.toiec.entity.QuestionGroupExam;
import com.toiec.toiec.exception.base.NotFoundException;
import com.toiec.toiec.repository.ExamRepository;
import com.toiec.toiec.repository.QuestionGroupExamRepository;
import com.toiec.toiec.repository.QuestionGroupRepository;
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
    private final QuestionGroupExamRepository questionGroupExamRepository;
    private final QuestionGroupRepository questionGroupRepository;
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

    @Override
    public Integer addQuestionGroupForExam(Integer examId,Integer questionGroupId,QuestionGroupRequest questionGroupRequest)
    {
        QuestionGroup questionGroup = questionGroupRepository.findById(questionGroupRequest.getIdQuestionGroup()).orElseThrow(
                NotFoundException::new
        );
        Exam exam = examRepository.findById(examId).orElseThrow(
                NotFoundException::new
        );
        List<QuestionGroupExam> questionGroupExamLst =questionGroupExamRepository.findByExamIdAndQuestionGroupId(examId,questionGroupId);
        if(questionGroupExamLst.size()!=0) {
            if (questionGroupExamLst.get(0).getOrderOfQuestionGroup() == questionGroupRequest.getOrderOfQuestionGroup()) {
                return 1;
            } else {
                questionGroupExamLst.get(0).setOrderOfQuestionGroup(questionGroupRequest.getOrderOfQuestionGroup());
                questionGroupExamRepository.save(questionGroupExamLst.get(0));
                return 1;
            }
        }
        QuestionGroupExam questionGroupExamNew = new QuestionGroupExam();
        questionGroupExamNew.setQuestionGroup(questionGroup);
        questionGroupExamNew.setExam(exam);
        questionGroupExamNew.setOrderOfQuestionGroup(questionGroupRequest.getOrderOfQuestionGroup());

        questionGroupExamRepository.save(questionGroupExamNew);
        return 1;
    }


    @Override
    public ExamPartDetailResponse getPartOfExamDetailById(Integer examId, String part) throws IOException
    {
        Exam exam = examRepository.findById(examId).orElseThrow(
                NotFoundException::new
        );
        ExamPartDetailResponse examDetailResponse = new ExamPartDetailResponse();
        List<Object[]> examDetailObjects=examRepository.findExamDetailsByPart(examId,part);

        if(!examDetailObjects.isEmpty()){
            examDetailResponse.setIdExam(examId);
            if(examDetailObjects.get(0)[1]!=null)examDetailResponse.setExamName((String) examDetailObjects.get(0)[1]);
        }


        List<ExamPartDetailResponse.QuestionGroupResponse> partResponses = new ArrayList<>();
        for(Object[] examDetailObject : examDetailObjects){
            if(examDetailObject[2]!=null)partResponses.addAll(JsonUtils.fromJsonList((String)examDetailObject[2],ExamPartDetailResponse.QuestionGroupResponse.class));
        }
        examDetailResponse.setQuestionGroups(partResponses);
        return examDetailResponse;
    }
}
