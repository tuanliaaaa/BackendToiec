package com.toiec.toiec.service;

import com.toiec.toiec.dto.request.history.HistoryRequest;
import com.toiec.toiec.dto.request.history.vocabulary.HistoryTopicRequest;
import com.toiec.toiec.dto.response.history.HistoryResponse;
import com.toiec.toiec.dto.response.history.lessonbypart.HistoryDetailLessonByPartResponse;
import com.toiec.toiec.dto.response.history.vocabulary.HistoryVocabularyResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface HistoryService {
    HistoryResponse createHistoryOfUser(String username, HistoryRequest historyRequest) ;
    HistoryResponse createWordHistoryOfUser(String username, HistoryTopicRequest historyTopicRequest);
    List<HistoryResponse> findHistoryOfUsernameByType(String username, String type,Integer page,Integer size);
    List<HistoryVocabularyResponse> findHistoryWordOfUsernameByType(String username, Integer page, Integer size);
    HistoryDetailLessonByPartResponse getHistoryPartByIdAndUsername(Integer idHistory, String usename) throws IOException;
    List<HistoryResponse> findHistoryExamOfUsername(String username,Integer page,Integer size) ;
}
