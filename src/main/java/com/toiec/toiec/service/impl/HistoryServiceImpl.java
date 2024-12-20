package com.toiec.toiec.service.impl;

import java.sql.Timestamp;
import com.toiec.toiec.dto.request.history.HistoryRequest;
import com.toiec.toiec.dto.request.history.vocabulary.HistoryTopicRequest;
import com.toiec.toiec.dto.request.history.QuestionRequest;
import com.toiec.toiec.dto.request.history.vocabulary.HistoryWordRequest;
import com.toiec.toiec.dto.response.history.HistoryResponse;
import com.toiec.toiec.dto.response.history.vocabulary.HistoryVocabularyResponse;
import com.toiec.toiec.entity.*;
import com.toiec.toiec.exception.answer.AnswerNotFoundException;
import com.toiec.toiec.exception.question.NumberOfQuestionsIsInsufficient;
import com.toiec.toiec.exception.question.QuestionNotFoundException;
import com.toiec.toiec.exception.user.UsernameNotFoundException;
import com.toiec.toiec.repository.*;
import com.toiec.toiec.service.HistoryService;
import com.toiec.toiec.service.LessonByPartService;
import com.toiec.toiec.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;
    private final UserAnswerRepository userAnswerRepository;
    private final UserRepository userRepository;
    private final LessonByPartService lessonByPartService;
    private final WordRepository wordRepository;
    private final TopicRepository topicRepository;
    private final HistoryDetailRepository historyDetailRepository;
    private final QuestionRepository questionRepository;
    private final QuestionGroupRepository questionGroupRepository;
    @Override
    public List<HistoryVocabularyResponse> findHistoryWordOfUsernameByType(String username, Integer page, Integer size) {
        List<Object[]> historiesList = historyRepository.findHistoriesWithDetailsByUsername(username,(page+1)*size,page*size);
        return historiesList.stream().map(result -> {
            HistoryVocabularyResponse dto = new HistoryVocabularyResponse();
            dto.setIdHistory((Integer) result[0]);
            dto.setType((String) result[1]);
            dto.setAmountQuestionGroup((Integer) result[2]);
            dto.setStatus((String) result[3]);
            Timestamp createdAtTimestamp = (Timestamp) result[4];
            dto.setCreatedAt(createdAtTimestamp != null ? createdAtTimestamp.toLocalDateTime() : null);

            Timestamp doneAtTimestamp = (Timestamp) result[5];
            dto.setDoneAt(doneAtTimestamp != null ? doneAtTimestamp.toLocalDateTime() : null);

            dto.setScore((Float) result[6]);
            dto.setUsername((String) result[7]);
            List<HistoryVocabularyResponse.HistoryDetailRequest> historyDetails = new ArrayList<>();
            try {
                historyDetails= JsonUtils.fromJsonList( (String) result[8],HistoryVocabularyResponse.HistoryDetailRequest.class);
            } catch (IOException e) {

            }
            dto.setHistoryDetails(historyDetails);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<HistoryResponse> findHistoryOfUsernameByType(String username, String type,Integer page,Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        List<History> historiesList = historyRepository.findByUser_UsernameAndTypeOrderByDoneAtDesc(username,type,pageable);
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
        questionRequestList=questionRequestList!=null?questionRequestList : new ArrayList<>();
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
        if(answerListId.isEmpty()) return 0;
        if(answerCorrectSet ==null) return 1;
        Set<Integer> answerMapID = new HashSet<>();
        for(int i=0;i<answerListId.size();i++)
        {
            if(!answerSet.contains(answerListId.get(i))) return -1;
            answerMapID.add(answerListId.get(i));
        }
        return answerCorrectSet.equals(answerMapID)?1:0;
    }
    @Override
    public HistoryResponse createWordHistoryOfUser(String username, HistoryTopicRequest historyTopicRequest) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException());
        List<LessonDetail> lessonDetailList = wordRepository.findByTopicWordId(historyTopicRequest.getTopicId());
        Map<Integer, LessonDetail> lessonDetailMap = lessonDetailList.stream()
                .collect(Collectors.toMap(LessonDetail::getIdLessonDetail, Function.identity()));
        History historyNew = new History();
        historyNew.setCreatedAt(LocalDateTime.now());
        historyNew.setUser(user);
        historyNew.setDoneAt(LocalDateTime.now());
        historyNew.setStatus("done");
        historyNew.setType("vocabulary");
        List<HistoryDetail> historyDetailList = new ArrayList<>();
        for (HistoryWordRequest word : historyTopicRequest.getWords()) {
            if (lessonDetailMap.containsKey(word.getId())) {
                HistoryDetail historyDetailNew = new HistoryDetail();
                historyDetailNew.setHistory(historyNew);
                historyDetailNew.setType("vocabulary");
                historyDetailNew.setScore(Math.min(word.getScore(), 2));
                historyDetailNew.setLessonDetail(lessonDetailMap.get(word.getId()));
                historyDetailList.add(historyDetailNew);
            }
        }
        historyNew = historyRepository.save(historyNew);
        historyDetailRepository.saveAll(historyDetailList);
        return new HistoryResponse(historyNew);
    }

}
