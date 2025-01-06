package com.toiec.toiec.service.impl;

import java.io.NotActiveException;
import java.sql.Timestamp;

import com.toiec.toiec.dto.request.history.HistoryRequest;
import com.toiec.toiec.dto.request.history.exam.CreateExamUser;
import com.toiec.toiec.dto.request.history.lessonbypart.HistoryLessonByPartRequest;
import com.toiec.toiec.dto.request.history.vocabulary.HistoryTopicRequest;
import com.toiec.toiec.dto.request.history.QuestionRequest;
import com.toiec.toiec.dto.request.history.vocabulary.HistoryWordRequest;
import com.toiec.toiec.dto.response.history.HistoryResponse;
import com.toiec.toiec.dto.response.history.learningpath.HistoryLearningPathResponse;
import com.toiec.toiec.dto.response.history.lessonbypart.HistoryDetailLessonByPartResponse;
//import com.toiec.toiec.dto.response.history.vocabulary.HistoryVocabularyResponse;
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
import com.toiec.toiec.utils.CriteriaPointUtils;
import com.toiec.toiec.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private final HistoryLessonRepository historyLessonRepository;
    private final UserAnswerRepository userAnswerRepository;
    private final UserRepository userRepository;
    private final LessonByPartService lessonByPartService;
    private final WordRepository wordRepository;
    private final TopicRepository topicRepository;
    private final HistoryDetailRepository historyDetailRepository;
    private final QuestionRepository questionRepository;
    private final QuestionGroupRepository questionGroupRepository;

    @Override
    public HistoryResponse createHistoryExamOfUser(String username, Integer examId, CreateExamUser createExamUser){
        User user = userRepository.findByUsername(username).orElseThrow(UsernameNotFoundException::new);
        List<Map<String,Object>> questionGroupListSQL= questionGroupRepository.findQuestionGroupsByExamId(examId);
        Map<Integer,Set<Integer>> questionWithAnsCorrect = new HashMap<>();
        Map<Integer,Set<Integer>> questionWithAnns = new HashMap<>();
        Map<Integer,Integer> questionQuestionGroup = new HashMap<>();
        for(Map<String,Object> questionGroupSQL : questionGroupListSQL){
            questionQuestionGroup.put((Integer) questionGroupSQL.get("questionId"),(Integer) questionGroupSQL.get("id_question_group"));
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
        historyNew.setType("exam");
        List<CreateExamUser.QuestionRequest> questionRequestList = createExamUser.getQuestionList();
        List<HistoryDetail> historyDetailNewList = new ArrayList<>();
        List<UserAnswer> userAnswerNewList = new ArrayList<>();
        Integer countCorrectListening=0,countCorrectReading=0,countQuestion=0;
        questionRequestList=questionRequestList!=null?questionRequestList : new ArrayList<>();
        if(questionRequestList.size()!=questionWithAnns.size()) throw new NumberOfQuestionsIsInsufficient();
        for(CreateExamUser.QuestionRequest questionRequest : questionRequestList){
            HistoryDetail historyDetailNew = new HistoryDetail();
            historyDetailNew.setHistory(historyNew);
            QuestionGroup questionGroupVr = new QuestionGroup();
            questionGroupVr.setIdQuestionGroup(questionQuestionGroup.get(questionRequest.getId()));
            historyDetailNew.setQuestionGroup(questionGroupVr);
            Question questionVir = new Question();
            questionVir.setIdQuestion(questionRequest.getId());
            historyDetailNew.setQuestion(questionVir);
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
                if(countQuestion<=100)countCorrectListening++;
                else countCorrectReading++;
                historyDetailNew.setIsCorrect(true);
            }
            for(int i=0;i<questionRequest.getAnswerList().size();i++){
                Answer answerVir = new Answer();
                answerVir.setId(questionRequest.getAnswerList().get(i));
                UserAnswer userAnswer= new UserAnswer(answerVir,historyDetailNew);
                userAnswerNewList.add(userAnswer);
            }
        }
        historyNew.setDoneAt(LocalDateTime.now());
        historyNew.setStatus("done");
        CriteriaPointUtils criteriaPointUtils = CriteriaPointUtils.getInstance();
        historyNew.setScore((float) criteriaPointUtils.getPointRedingByCount(countCorrectReading)+criteriaPointUtils.getPointListeningByCount(countCorrectListening));
        historyRepository.save(historyNew);
        historyDetailRepository.saveAll(historyDetailNewList);
        userAnswerRepository.saveAll(userAnswerNewList);
        return new HistoryResponse(historyNew);
    }



    @Override
    public HistoryResponse createHistoryLessonByPartOfUsernameByType(String username, HistoryLessonByPartRequest historyLessonByPartRequest)
    {
        User user = userRepository.findByUsername(username).orElseThrow(UsernameNotFoundException::new);
        Pageable pageable = PageRequest.of(0, 1, Sort.by("createdAt").descending());
        Page<HistoryLesson> historyLessons= historyLessonRepository.findByUser_UsernameOrderByCreatedAtDesc( username,  pageable);
        Optional<HistoryLesson> firstHistoryLesson = historyLessons.getContent().stream().findFirst();
        List<History> historySQL = historyRepository.findByHistoryLesson_IdHistoryLessonAndLessonDetail_IdLessonDetail(firstHistoryLesson.get().getIdHistoryLesson(),historyLessonByPartRequest.getLessonDetailId());
        History historyNew = new History();
        if(historySQL.size()==0){
            historyNew.setHistoryLesson(firstHistoryLesson.get());
            LessonDetail lessonDetailVR = new LessonDetail();
            lessonDetailVR.setIdLessonDetail(historyLessonByPartRequest.getLessonDetailId());
            historyNew.setLessonDetail(lessonDetailVR);
            historyNew.setType("learningpath");
            historyNew.setDoneAt(LocalDateTime.now());
            historyNew.setCreatedAt(LocalDateTime.now());
            historyNew.setStatus("done");
            historyNew.setUser(user);
            historyNew=historyRepository.save(historyNew);
        }else{
            historyNew = historySQL.get(0);
        }

        return new HistoryResponse(historyNew);
    }


    @Override
    public List<HistoryLearningPathResponse> findHistoryLearningPathOfUsernameByType(String username, Integer page, Integer size,Integer idLesson) {
        List<Object[]> his = historyRepository.findHistoriesLearningPathWithDetailsByUsername(username,(page+1)*size,page*size,"learningpath");

        return his.stream().map(result -> {
            HistoryLearningPathResponse dto = new HistoryLearningPathResponse();
            dto.setIdHistoryLesson((Integer) result[0]);
            dto.setType((String) result[1]);
            Timestamp createdAtTimestamp = (Timestamp) result[2];
            dto.setUsername((String) result[3]);
            dto.setCreatedAt(createdAtTimestamp != null ? createdAtTimestamp.toLocalDateTime() : null);
            List<HistoryLearningPathResponse.HistoryResponse> historyDetails = new ArrayList<>();
            try {
                historyDetails= JsonUtils.fromJsonList( (String) result[4],HistoryLearningPathResponse.HistoryResponse.class);
            } catch (IOException e) {

            }
            dto.setHistoryDetails(historyDetails);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<HistoryVocabularyResponse> findHistoryWordOfUsernameByType(String username, Integer page, Integer size,Integer idLesson) {
        List<Object[]> his = historyRepository.findHistoriesVocabularyWithDetailsByUsername(username,(page+1)*size,page*size,"vocabulary",idLesson);

        return his.stream().map(result -> {
            HistoryVocabularyResponse dto = new HistoryVocabularyResponse();
            dto.setIdHistoryLesson((Integer) result[0]);
            dto.setType((String) result[1]);
            dto.setScore( Double.valueOf(result[2].toString()));
            Timestamp createdAtTimestamp = (Timestamp) result[3];
            dto.setUsername((String) result[4]);
            dto.setIdLesson((Integer) result[5]);
            dto.setLessonName((String) result[6]);
            dto.setCreatedAt(createdAtTimestamp != null ? createdAtTimestamp.toLocalDateTime() : null);
            List<HistoryVocabularyResponse.HistoryResponse> historyDetails = new ArrayList<>();
            try {
                historyDetails= JsonUtils.fromJsonList( (String) result[7],HistoryVocabularyResponse.HistoryResponse.class);
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
        Map<Integer,Integer> questionQuestionGroup = new HashMap<>();
        for(Map<String,Object> questionGroupSQL : questionGroupListSQL){
            questionQuestionGroup.put((Integer) questionGroupSQL.get("questionId"),(Integer) questionGroupSQL.get("id_question_group"));
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
            QuestionGroup questionGroupVr = new QuestionGroup();
            questionGroupVr.setIdQuestionGroup(questionQuestionGroup.get(questionRequest.getId()));
            historyDetailNew.setQuestionGroup(questionGroupVr);
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
    public HistoryVocabularyResponse createWordHistoryOfUser(String username, HistoryTopicRequest historyTopicRequest) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException());
        List<LessonDetail> lessonDetailList = wordRepository.findByTopicWordId(historyTopicRequest.getTopicId());
        Lesson lesson = topicRepository.findById(historyTopicRequest.getTopicId()).orElseThrow(NotFoundException::new);
        Map<Integer, LessonDetail> lessonDetailMap = lessonDetailList.stream()
                .collect(Collectors.toMap(LessonDetail::getIdLessonDetail, Function.identity()));
        HistoryLesson historyLessonNew = new HistoryLesson();
        historyLessonNew.setLesson(lesson);
        historyLessonNew.setUser(user);
        historyLessonNew.setIsCorrect(true);
        historyLessonNew.setCreatedAt(LocalDateTime.now());
        historyLessonNew.setType("vocabulary");
        List<History> historyListNew = new ArrayList<>();
        for (HistoryWordRequest word : historyTopicRequest.getWords()) {
            if (lessonDetailMap.containsKey(word.getId())) {
                History historyNew = new History();
                historyNew.setCreatedAt(LocalDateTime.now());
                historyNew.setUser(user);
                historyNew.setDoneAt(LocalDateTime.now());
                historyNew.setStatus("done");
                historyNew.setType("vocabulary");
                historyNew.setScore((float) Math.min(word.getScore(), 2));
                historyNew.setHistoryLesson(historyLessonNew);
                historyNew.setLessonDetail(lessonDetailMap.get(word.getId()));
                historyListNew.add(historyNew);
            }
        }
        historyLessonNew = historyLessonRepository.save(historyLessonNew);
        historyRepository.saveAll(historyListNew);
        HistoryVocabularyResponse historyVocabularyResponse= new HistoryVocabularyResponse();
        historyVocabularyResponse.setIdLesson(lesson.getIdLesson());
        historyVocabularyResponse.setCreatedAt(historyLessonNew.getCreatedAt());
        historyVocabularyResponse.setIdHistoryLesson(historyLessonNew.getIdHistoryLesson());
        historyVocabularyResponse.setType(historyVocabularyResponse.getType());
        return historyVocabularyResponse;
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
