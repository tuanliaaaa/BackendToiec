package com.toiec.toiec.service.impl;

import com.toiec.toiec.dto.response.lessons.Answer;
import com.toiec.toiec.dto.response.lessons.Question;
import com.toiec.toiec.dto.response.lessons.Resource;
import com.toiec.toiec.repository.QuestionRepository;
import com.toiec.toiec.service.LessonByPartService;
import com.toiec.toiec.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
public class LessonByPartServiceImpl implements LessonByPartService {
    @Autowired
    private final QuestionRepository questionRepository;
    @Override

    public List<Question> getQuestionsByType(String type, int limit) throws IOException {
        List<Object[]> objList = questionRepository.findQuestionsWithAnswersByType(type, limit);
        List<Question> questions = new ArrayList<>();

        for (Object[] o : objList) {
            Integer questionId = (Integer) o[0];
            String questionValue = (String) o[1];

            Question question = new Question(questionId, questionValue, new ArrayList<>(), new ArrayList<>());
            questions.add(question);
            if(o[2]!=null){
                List<Answer> answerList= JsonUtils.fromJsonList((String)o[2],Answer.class);
                question.setAnswerList(answerList);
            }
            if(o[3]!=null){
                List<Resource> resourceList= JsonUtils.fromJsonList((String)o[3],Resource.class);
                question.setResourceList(resourceList);
            }
        }
        return questions;
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
