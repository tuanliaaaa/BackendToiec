package com.toiec.toiec.service.impl;

import java.io.NotActiveException;
import java.sql.Timestamp;

import com.toiec.toiec.dto.request.history.HistoryRequest;
import com.toiec.toiec.dto.request.history.vocabulary.HistoryTopicRequest;
import com.toiec.toiec.dto.request.history.QuestionRequest;
import com.toiec.toiec.dto.request.history.vocabulary.HistoryWordRequest;
import com.toiec.toiec.dto.response.history.HistoryResponse;
import com.toiec.toiec.dto.response.history.lessonbypart.HistoryDetailLessonByPartResponse;
import com.toiec.toiec.dto.response.history.vocabulary.HistoryVocabularyResponse;
import com.toiec.toiec.entity.*;
import com.toiec.toiec.exception.answer.AnswerNotFoundException;
import com.toiec.toiec.exception.base.NotFoundException;
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
        List<Object[]> his = historyRepository.findHistoriesVocabularyWithDetailsByUsername(username,(page+1)*size,page*size);

        return his.stream().map(result -> {
            HistoryVocabularyResponse dto = new HistoryVocabularyResponse();
            dto.setIdHistory((Integer) result[0]);
            dto.setType((String) result[1]);
            dto.setScore((Double) result[2]);
            Timestamp createdAtTimestamp = (Timestamp) result[3];
            dto.setIdLesson((Integer) result[4]);
            dto.setLessonName((String) result[5]);
            dto.setCreatedAt(createdAtTimestamp != null ? createdAtTimestamp.toLocalDateTime() : null);
            List<HistoryVocabularyResponse.HistoryDetailResponse> historyDetails = new ArrayList<>();
            try {
                historyDetails= JsonUtils.fromJsonList( (String) result[6],HistoryVocabularyResponse.HistoryDetailResponse.class);
            } catch (IOException e) {

            }
            dto.setHistoryDetails(historyDetails);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<HistoryResponse> findHistoryExamOfUsername(String username,Integer page,Integer size)
    {
        Pageable pageable = PageRequest.of(page, size);
        List<History> historiesList = historyRepository.findByUser_UsernameAndTypeOrderByDoneAtDesc(username,"exam",pageable);
        List<HistoryResponse> historyResponseList = new ArrayList<>();
        for(History history : historiesList){
            historyResponseList.add(new HistoryResponse(history));
        }
        return historyResponseList;
    }

    @Override
    public List<HistoryResponse> findHistoryOfUsernameByType(String username, String type,Integer page,Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        type=(type==null)?"part":type;
        List<History> historiesList = historyRepository.findByUser_UsernameAndTypeContainingOrderByDoneAtDesc(username,type,pageable);
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

    @Override
    public HistoryDetailLessonByPartResponse getHistoryPartByIdAndUsername(Integer idHistory, String usename) throws IOException {
        List<Object[]> hisLst = historyRepository.findHistoryOfLessonByIdHistoryAndUsername(idHistory);
        Set<String> lstQuestionCorrectId = new HashSet<>();
        for(Object[] his: hisLst)
        {
            if((Boolean) his[7]) lstQuestionCorrectId.add(his[9]+"-"+his[8]);
        }





        List<Object[]> objects = historyRepository.findHistoryWithExamDetails(idHistory);
        if (objects.size() == 0) throw new NotFoundException();

        Map<Integer,HistoryDetailLessonByPartResponse.QuestionGroupResponse.Question.Answer> answerResponseMap = new HashMap<>();
        Map<String, HistoryDetailLessonByPartResponse.QuestionGroupResponse.Question.Answer> questionIdAndAnswerMapResponse = new HashMap<>();
        Map<String, HistoryDetailLessonByPartResponse.QuestionGroupResponse.Question> questionGroupIdAndquestionMapResponse = new HashMap<>();
        Map<String, HistoryDetailLessonByPartResponse.QuestionGroupResponse.Resource> questionGroupIdAndresourceMapResponse = new HashMap<>();
        Map<Integer, HistoryDetailLessonByPartResponse.QuestionGroupResponse> questionGroupResponseMap = new HashMap<>();
        Set<Integer> lstAnswerSelectedId = new HashSet<>();
        for (Object[] object : objects) {
            if(!answerResponseMap.containsKey((Integer) object[10])) {
                answerResponseMap.put((Integer) object[10],new HistoryDetailLessonByPartResponse.QuestionGroupResponse.Question.Answer(
                    (Integer) object[10],(String) object[11],(Boolean)  object[12],false
                ));
            }
            lstAnswerSelectedId.add((Integer) object[17]);
            //   Thêm mới question  group nếu chưa có
            if(!questionGroupResponseMap.containsKey((Integer) object[6]))
            {
                questionGroupResponseMap.put((Integer) object[6],new HistoryDetailLessonByPartResponse.QuestionGroupResponse(
                        (Integer) object[6],(String) object[7],new ArrayList<>(),new ArrayList<>()
                ));
            }

            // THêm tài nguyên cho question group
            if(!questionGroupIdAndresourceMapResponse.containsKey(object[6]+"-"+ object[13]))
            {
                questionGroupIdAndresourceMapResponse.put(object[6]+"-"+ object[13],new HistoryDetailLessonByPartResponse.QuestionGroupResponse.Resource(
                        (Integer) object[13],(String) object[15],(String) object[14]
                ));

                questionGroupResponseMap.get((Integer) object[6]).getResourceList().add(questionGroupIdAndresourceMapResponse.get(object[6]+"-"+ object[13]));

            }

            // Thêm question cho question group
            if(!questionGroupIdAndquestionMapResponse.containsKey(object[6]+"-"+ object[8]))
            {
                questionGroupIdAndquestionMapResponse.put(object[6]+"-"+ object[8],new HistoryDetailLessonByPartResponse.QuestionGroupResponse.Question(
                        (Integer) object[8],(String) object[9],new ArrayList<>(),false
                ));
                questionGroupResponseMap.get((Integer) object[6]).getQuestionList().add(questionGroupIdAndquestionMapResponse.get(object[6]+"-"+ object[8]));
            }

            // Thêm answer cho question
            if(!questionIdAndAnswerMapResponse.containsKey(object[8]+"-"+ object[10]))
            {
                questionIdAndAnswerMapResponse.put( object[8]+"-"+ object[10],answerResponseMap.get((Integer) object[10]));
                questionGroupIdAndquestionMapResponse.get(object[6]+"-"+ object[8]).getAnswerList().add(questionIdAndAnswerMapResponse.get(object[8]+"-"+ object[10]));
            }

        }

        // Sửa trạng thái userSeleted trong obj answerResponseMap để cập nhật trạng thái chọn của người dùng
        for(Integer answerId:lstAnswerSelectedId)
        {
            if(answerResponseMap.containsKey(answerId))
            {
                answerResponseMap.get(answerId).setIsUserSelect(true);
            }
        }

        // Sửa trạng thái đúng sai khi làm bài của ngời dùng
        for(String questionCorrectId:lstQuestionCorrectId)
        {
            if(questionGroupIdAndquestionMapResponse.containsKey(questionCorrectId))
            {
                questionGroupIdAndquestionMapResponse.get(questionCorrectId).setIsCorrect(true);
            }
        }

        HistoryDetailLessonByPartResponse historyDetailLessonByPartResponse= new HistoryDetailLessonByPartResponse(objects.get(0));
        historyDetailLessonByPartResponse.setQuestionGroups(new ArrayList<>(questionGroupResponseMap.values()));
        return  historyDetailLessonByPartResponse;
    }



}
