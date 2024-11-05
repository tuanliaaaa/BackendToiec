package com.toiec.toiec.service.impl;

import com.toiec.toiec.dto.request.history.HistoryRequest;
import com.toiec.toiec.dto.request.history.QuestionRequest;
import com.toiec.toiec.dto.response.history.HistoryResponse;
import com.toiec.toiec.entity.Histories;
import com.toiec.toiec.entity.HistoryDetail;
import com.toiec.toiec.entity.Question;
import com.toiec.toiec.entity.User;
import com.toiec.toiec.repository.HistoryDetailRepository;
import com.toiec.toiec.repository.HistoryRepository;
import com.toiec.toiec.repository.QuestionRepository;
import com.toiec.toiec.repository.UserRepository;
import com.toiec.toiec.service.HistoryService;
import com.toiec.toiec.service.LessonByPartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final LessonByPartService lessonByPartService;
    private final HistoryDetailRepository historyDetailRepository;
    private final QuestionRepository questionRepository;
    @Override
    public HistoryResponse getLastNHistoryByUsernameAndType(String username, String type) {
        Pageable pageable = PageRequest.of(0, 1);
        List<Histories> historiesList = historyRepository.findByUser_UsernameAndTypeOrderByDateDesc(username,type,pageable);
        if(historiesList.isEmpty())return null;
        else return new HistoryResponse(historiesList.get(0));
    }
    @Override
    public HistoryResponse createHistoryOfUser(String username, HistoryRequest historyRequest) throws IOException {
        User user = userRepository.findByUsername(username).orElseThrow();
        Histories historyNew = new Histories();
        historyNew.setDate(LocalDateTime.now());
        historyNew.setUser(user);
        historyNew.setType(historyRequest.getType());
        List<QuestionRequest> questionRequestList = historyRequest.getQuestionList();

        List<HistoryDetail> historyDetailNewList = new ArrayList<>();

        Integer countCorrext=0,countQuestion=0;
        for(QuestionRequest questionRequest : questionRequestList){
            HistoryDetail historyDetailNew = new HistoryDetail();
            historyDetailNew.setHistory(historyNew);
            historyDetailNew.setAnswerSeleted(questionRequest.getAnswerList());
            Question questionVir = new Question();
            questionVir.setIdQuestion(questionRequest.getId());
            historyDetailNew.setQuestion(questionVir);
            historyDetailNew.setIsCorrect(questionRequest.getIsCorrect());
            historyDetailNewList.add(historyDetailNew);
            countQuestion++;
            if(questionRequest.getIsCorrect()){
                countCorrext++;
            }
        }
        historyNew.setAmountQuestion(countQuestion);
        historyNew.setCorrectQuestion(countCorrext);
        historyRepository.save(historyNew);

        historyDetailRepository.saveAll(historyDetailNewList);
        return new HistoryResponse(historyNew);
    }

}
