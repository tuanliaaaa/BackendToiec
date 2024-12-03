package com.toiec.toiec.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toiec.toiec.dto.response.lesson.Question;
import com.toiec.toiec.dto.response.lesson.QuestionGroupResponse;
import com.toiec.toiec.dto.response.lesson.Resource;
import com.toiec.toiec.exception.type.TypeNotFoundException;
import com.toiec.toiec.repository.QuestionGroupRepository;
import com.toiec.toiec.repository.QuestionRepository;
import com.toiec.toiec.service.LessonByPartService;
import com.toiec.toiec.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
public class LessonByPartServiceImpl implements LessonByPartService {
    @Autowired
    private final QuestionRepository questionRepository;
    private final QuestionGroupRepository questionGroupRepository;
    @Autowired
    private final ObjectMapper objectMapper;
    @Override

    public List<QuestionGroupResponse> getQuestionsByType(String type, int limit) throws IOException {
        String []typeListOneQuestion={"part1","part2","part5","part3","part4","part6","part7"};
        if(!Arrays.asList(typeListOneQuestion).contains(type)) throw new TypeNotFoundException();
        List<QuestionGroupResponse> questionPartList= new ArrayList<>();

        List<Object[]> objList = questionGroupRepository.findParentQuestionGroupsWithChildQuestionsAndSharedResources(type, limit);

        for (Object[] o : objList) {
            QuestionGroupResponse questionPart = new QuestionGroupResponse();
            questionPart.setId(Integer.parseInt(o[0].toString()));
            if(o[1]!=null) questionPart.setName(o[1].toString());
            if(o[2]!=null){
                List<Question> questionList= JsonUtils.fromJsonList((String)o[2],Question.class);
                questionPart.setQuestionList(questionList);
            }
            if(o[3]!=null){
                List<Resource> resourceList= JsonUtils.fromJsonList((String)o[3],Resource.class);
                questionPart.setResourceList(resourceList);
            }
            questionPartList.add(questionPart);
        }

        return questionPartList;
    }

//    public Question historyPartOfUser(String type, int limit) {
//        List<Object[]> obj=questionRepository.findQuestionsWithAnswersByType(type,limit);
//        List<Answer> answers = new ArrayList<>();
//        for(Object[] o:obj)
//        {
//          //  answers.add(new Answer((Integer) o[2],(String) o[3]));
//        }
//        return new Question((Integer) obj.get(0)[0],(String)obj.get(0)[1],answers );
//    }
}
