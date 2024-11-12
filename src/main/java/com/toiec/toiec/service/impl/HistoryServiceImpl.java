package com.toiec.toiec.service.impl;

import com.toiec.toiec.dto.request.history.HistoryRequest;
import com.toiec.toiec.dto.request.history.QuestionRequest;
import com.toiec.toiec.dto.response.history.HistoryResponse;
import com.toiec.toiec.entity.*;
import com.toiec.toiec.exception.answer.AnswerNotFoundException;
import com.toiec.toiec.exception.question.NumberOfQuestionsIsInsufficient;
import com.toiec.toiec.exception.question.QuestionNotFoundException;
import com.toiec.toiec.exception.user.UsernameNotFoundException;
import com.toiec.toiec.repository.*;
import com.toiec.toiec.service.HistoryService;
import com.toiec.toiec.service.LessonByPartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;
    private final UserAnswerRepository userAnswerRepository;
    private final UserRepository userRepository;
    private final LessonByPartService lessonByPartService;
    private final HistoryDetailRepository historyDetailRepository;
    private final QuestionRepository questionRepository;
    private final QuestionGroupRepository questionGroupRepository;
    @Override
    public List<HistoryResponse> findHistoryOfUsernameByType(String username, String type,Integer page,Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        List<History> historiesList = historyRepository.findByUser_UsernameAndTypeOrderByDoneAt(username,type,pageable);
        List<HistoryResponse> historyResponseList = new ArrayList<>();
        for(History history : historiesList){
            historyResponseList.add(new HistoryResponse(history));
        }
        return historyResponseList;
    }
    @Override
    public HistoryResponse createHistoryOfUser(String username, HistoryRequest historyRequest){
        User user = userRepository.findByUsername(username).orElseThrow(UsernameNotFoundException::new);
        List<Map<String,Object>> questionGroupListSQL= questionGroupRepository.findQuestionGroups(historyRequest.getType(),historyRequest.getAmountQuestionGroup(),0);
        Map<Integer,Set<Integer>> questionWithAnsCorrect = new HashMap<>();
        Map<Integer,Set<Integer>> questionWithAnns = new HashMap<>();

        for(Map<String,Object> questionGroupSQL : questionGroupListSQL){
            Set<Integer> ans= questionWithAnns.getOrDefault((Integer) questionGroupSQL.get("questionId"),new HashSet<>());
            ans.add((Integer) questionGroupSQL.get("answerId"));
            questionWithAnns.put((Integer) questionGroupSQL.get("questionId"),ans);
            if((boolean) questionGroupSQL.get("correct")){
                Set<Integer> ansCorrect = questionWithAnsCorrect.getOrDefault((Integer) questionGroupSQL.get("questionId"),new HashSet<>());
                ansCorrect.add((Integer) questionGroupSQL.get("answerId"));
                questionWithAnsCorrect.put((Integer) questionGroupSQL.get("questionId"),ansCorrect);
            }
        }
        History historyNew = new History();
        historyNew.setCreatedAt(LocalDateTime.now());
        historyNew.setUser(user);
        historyNew.setType(historyRequest.getType());
        List<QuestionRequest> questionRequestList = historyRequest.getQuestionList();
        List<HistoryDetail> historyDetailNewList = new ArrayList<>();
        List<UserAnswer> userAnswerNewList = new ArrayList<>();
        Integer countCorrect=0,countQuestion=0;
        if(questionRequestList.size()!=questionWithAnns.size()) throw new NumberOfQuestionsIsInsufficient();
        for(QuestionRequest questionRequest : questionRequestList){
            HistoryDetail historyDetailNew = new HistoryDetail();
            historyDetailNew.setHistory(historyNew);
            Question questionVir = new Question();
            questionVir.setIdQuestion(questionRequest.getId());
            historyDetailNew.setQuestion(questionVir);
            historyDetailNew.setIsCorrect(questionRequest.getIsCorrect());
            historyDetailNewList.add(historyDetailNew);
            countQuestion++;
            if(questionWithAnns.get(questionRequest.getId())==null) throw new QuestionNotFoundException();
            Integer checkAsr=checkAnswer(questionWithAnns.get(questionRequest.getId()),questionWithAnsCorrect.get(questionRequest.getId()),questionRequest.getAnswerList());
            if(checkAsr==-1)
            {
                throw new AnswerNotFoundException();
            } else if (checkAsr==0) {
                historyDetailNew.setIsCorrect(false);
            }else{
                countCorrect++;
                historyDetailNew.setIsCorrect(true);
            }
            for(int i=0;i<questionRequest.getAnswerList().size();i++){
                Answer answerVir = new Answer();
                answerVir.setId(questionRequest.getAnswerList().get(i));
                UserAnswer userAnswer= new UserAnswer(answerVir,historyDetailNew);
                userAnswerNewList.add(userAnswer);
            }
        }
        historyNew.setAmountQuestionGroup(historyRequest.getAmountQuestionGroup());
        historyNew.setDoneAt(LocalDateTime.now());
        historyNew.setStatus("done");
        historyNew.setScore((float) countCorrect/countQuestion*10);
        historyRepository.save(historyNew);
        historyDetailRepository.saveAll(historyDetailNewList);
        userAnswerRepository.saveAll(userAnswerNewList);
        return new HistoryResponse(historyNew);
    }
    //Kiểm tra xem answer với các trạng thái : {-1:"Lựa chọn của bạn trong cơ sở dữ liệu không tồn tại ",0:"Lựa chọn không đúng",1:"Lựa chọn của bạn là đúng"}
    private static Integer checkAnswer(Set answerSet,Set answerCorrectSet,List<Integer> answerListId)
    {
        if(answerListId.isEmpty()) return -1;
        Set<Integer> answerMapID = new HashSet<>();
        for(int i=0;i<answerListId.size();i++)
        {
            if(!answerSet.contains(answerListId.get(i))) return -1;
            answerMapID.add(answerListId.get(i));
        }
        return answerCorrectSet.equals(answerMapID)?1:0;
    }
}
