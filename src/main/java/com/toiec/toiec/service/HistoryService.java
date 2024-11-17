package com.toiec.toiec.service;

import com.toiec.toiec.dto.request.history.HistoryRequest;
import com.toiec.toiec.dto.request.history.HistoryTopicRequest;
import com.toiec.toiec.dto.request.history.HistoryWordRequest;
import com.toiec.toiec.dto.response.history.HistoryResponse;
import com.toiec.toiec.entity.History;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface HistoryService {
    HistoryResponse createHistoryOfUser(String username, HistoryRequest historyRequest) ;
    HistoryResponse createWordHistoryOfUser(String username, HistoryTopicRequest historyTopicRequest);
    List<HistoryResponse> findHistoryOfUsernameByType(String username, String type,Integer page,Integer size);
}
