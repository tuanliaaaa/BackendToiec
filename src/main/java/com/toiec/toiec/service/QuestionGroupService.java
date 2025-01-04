package com.toiec.toiec.service;

import com.toiec.toiec.dto.request.questiongroups.QuestionGroupRequest;
import com.toiec.toiec.dto.response.questiongroup.QuestionGroupResponse;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
public interface QuestionGroupService {
    QuestionGroupResponse getQuestionGroupByID(Integer questionGroupId) throws IOException;
    List<QuestionGroupResponse> searchQuestionGroup(String value,String type,Integer page,Integer size) throws IOException;
    void createQuestionGroup(QuestionGroupRequest questionGroupDto, List<MultipartFile> files);
}
