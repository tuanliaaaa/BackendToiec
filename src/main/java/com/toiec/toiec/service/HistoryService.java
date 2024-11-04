package com.toiec.toiec.service;

import com.toiec.toiec.dto.response.history.HistoryResponse;
import com.toiec.toiec.entity.Histories;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HistoryService {

    HistoryResponse getLastNHistoryByUsernameAndType(String username, String type);
}
