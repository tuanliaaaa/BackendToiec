package com.toiec.toiec.service;

import com.toiec.toiec.dto.response.lessons.Question;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface LessonByPartService {

    List<Question> getQuestionsByType(String type, int limit) throws IOException;
//    Question historyPartOfUser(String type, int limit) ;
}
