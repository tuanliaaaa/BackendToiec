package com.toiec.toiec.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toiec.toiec.dto.response.lessons.Answer;
import com.toiec.toiec.dto.response.lessons.Question;
import com.toiec.toiec.dto.response.lessons.QuestionPart;
import com.toiec.toiec.dto.response.lessons.Resource;
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
    @Autowired
    private final ObjectMapper objectMapper;
    @Override

    public List<QuestionPart> getQuestionsByType(String type, int limit) throws IOException {
        String []typeListOneQuestion={"part1","part2","part5"};
        String []typeListMutipleQuestion={"part3","part4","part6","part7"};
        List<QuestionPart> questionPartList= new ArrayList<>();
        if(Arrays.asList(typeListOneQuestion).contains(type))
        {
            List<Object[]> objList = questionRepository.findQuestionsWithAnswersByType(type, limit);

            for (Object[] o : objList) {
                QuestionPart questionPart = new QuestionPart();
                List<Question> questions = new ArrayList<>();
                Integer questionId = (Integer) o[0];
                String questionValue = (String) o[1];

                Question question = new Question(questionId, questionValue, new ArrayList<>());
                questions.add(question);

                if(o[2]!=null){
                    List<Answer> answerList= JsonUtils.fromJsonList((String)o[2],Answer.class);
                    question.setAnswerList(answerList);
                    questionPart.setQuestionList(questions);
                }
                if(o[3]!=null){
                    List<Resource> resourceList= JsonUtils.fromJsonList((String)o[3],Resource.class);
                    questionPart.setResourceList(resourceList);
                }
                questionPartList.add(questionPart);
            }
        }
        else{
            List<Object[]> objList = questionRepository.findParentQuestionsWithChildQuestionsAndSharedResources(type, limit);

            for (Object[] o : objList) {
                QuestionPart questionPart = new QuestionPart();
                if(o[0]!=null){
                    List<Question> questionList= JsonUtils.fromJsonList((String)o[0],Question.class);
                    questionPart.setQuestionList(questionList);
                }
                if(o[1]!=null){
                    List<Resource> resourceList= JsonUtils.fromJsonList((String)o[1],Resource.class);
                    questionPart.setResourceList(resourceList);
                }
                questionPartList.add(questionPart);
            }
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
