package com.toiec.toiec.service;

import com.toiec.toiec.dto.response.lessons.Question;
import com.toiec.toiec.dto.response.lessons.QuestionGroupResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface LessonByPartService {

    List<QuestionGroupResponse> getQuestionsByType(String type, int limit) throws IOException;
//    Question historyPartOfUser(String type, int limit) ;
}
