package com.toiec.toiec.service.impl;

import com.toiec.toiec.dto.response.history.HistoryResponse;
import com.toiec.toiec.entity.Histories;
import com.toiec.toiec.repository.HistoryRepository;
import com.toiec.toiec.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;

    @Override
    public HistoryResponse getLastNHistoryByUsernameAndType(String username, String type) {
        Pageable pageable = PageRequest.of(0, 1);
        List<Histories> historiesList = historyRepository.findByUser_UsernameAndTypeOrderByDateDesc(username,type,pageable);
        if(historiesList.isEmpty())return null;
        else return new HistoryResponse(historiesList.get(0));
    }
}
