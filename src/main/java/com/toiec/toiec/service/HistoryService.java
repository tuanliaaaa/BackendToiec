package com.toiec.toiec.service;

import com.toiec.toiec.dto.request.history.HistoryRequest;
import com.toiec.toiec.dto.response.history.HistoryResponse;
import com.toiec.toiec.entity.Histories;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface HistoryService {
    HistoryResponse createHistoryOfUser(String username, HistoryRequest historyRequest) throws IOException;
    HistoryResponse getLastNHistoryByUsernameAndType(String username, String type);
}
