package com.toiec.toiec.service.impl;

import com.toiec.toiec.dto.response.lessons.Question;
import com.toiec.toiec.dto.response.lessons.QuestionGroupResponse;
import com.toiec.toiec.dto.response.lessons.Resource;
import com.toiec.toiec.dto.response.topicwords.TopicResponse;
import com.toiec.toiec.dto.response.topicwords.TopicWordResponse;
import com.toiec.toiec.dto.response.topicwords.WordResponse;
import com.toiec.toiec.entity.Lesson;
import com.toiec.toiec.entity.LessonDetail;
import com.toiec.toiec.exception.base.NotFoundException;
import com.toiec.toiec.exception.question.QuestionNotFoundException;
import com.toiec.toiec.exception.user.UsernameNotFoundException;
import com.toiec.toiec.repository.QuestionGroupRepository;
import com.toiec.toiec.repository.TopicRepository;
import com.toiec.toiec.repository.WordRepository;
import com.toiec.toiec.service.TopicWordService;
import com.toiec.toiec.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TopicWordServiceImpl implements TopicWordService {
    private final TopicRepository topicRepository;
    private final WordRepository wordRepository;
    private final QuestionGroupRepository questionGroupRepository;

    @Override
    public List<TopicWordResponse> getAllTopicWords() {
       List<TopicWordResponse> topicWordLstResponse = new ArrayList<>();
        List<Object[]> objects = topicRepository.findAllVocabularyTopics();
        for(Object[] object: objects){
            TopicWordResponse topicWordResponse = new TopicWordResponse();
            topicWordResponse.setId((Integer)object[0]);
            topicWordResponse.setName((String)object[1]);
            topicWordLstResponse.add(topicWordResponse);
        }
        return topicWordLstResponse;
    }
//
//    @Override
//    public TopicWordResponse addTopicWord(CreateTopicWordRequest request) {
//        try {
//            TopicWord topicWord = new TopicWord();
//            topicWord.setNameTopicWord(request.getName());
//            topicWord = topicWordRepository.save(topicWord);
//            return toResponse(topicWord);
//        } catch (Exception e) {
//            throw new RuntimeException("Error creating new topic word", e);
//        }
//    }
//
    @Override
    public TopicResponse getWordsByTopicId(int topicId) throws IOException {
        TopicResponse topicResponse = new TopicResponse();
        List<Object[]> objects = topicRepository.findLessonWithDetails(topicId);
        if(objects.size()==0) throw new NotFoundException();
        topicResponse.setId(topicId);
        topicResponse.setName((String)objects.get(0)[1]);
        if(objects.get(0)[3]!=null){
            List<WordResponse> questionList= JsonUtils.fromJsonList((String)objects.get(0)[3],WordResponse.class);
            topicResponse.setWords(questionList);
        }
        return topicResponse;
    }
    @Override
    public List<QuestionGroupResponse> getExercisesByTopicId(int topicId) throws IOException {
        List<QuestionGroupResponse> questionPartList= new ArrayList<>();

        List<Object[]> objects = questionGroupRepository.findParentQuestionGroupsWithChildQuestionsAndResourcesAndLessonDetails(topicId);

        for (Object[] o : objects) {
            QuestionGroupResponse questionPart = new QuestionGroupResponse();
            questionPart.setId(Integer.parseInt(o[0].toString()));
            questionPart.setName(o[1].toString());
            if(o[2]!=null){
                List<Question> questionList= JsonUtils.fromJsonList((String)o[2],Question.class);
                questionPart.setQuestionList(questionList);
            }
            if(o[3]!=null){
                List<Resource> resourceList= JsonUtils.fromJsonList((String)o[3],Resource.class);
                questionPart.setResourceList(resourceList);
            }
            questionPartList.add(questionPart);
        }

        return questionPartList;
    }

//
//    @Override
//    public WordResponse addWordToTopic(int topicId, CreateWordRequest request) {
//        try {
//            TopicWord topicWord = topicWordRepository.findById(topicId)
//                    .orElseThrow(() -> new RuntimeException("Topic not found"));
//            Word word = new Word();
//            word.setTopicWord(topicWord);
//            word.setWord(request.getWord());
//            word.setMean(request.getMean());
//            word.setType(request.getType());
//            word = wordRepository.save(word);
//            return toWordResponse(word);
//        } catch (Exception e) {
//            throw new RuntimeException("Error adding word to topic with id: " + topicId, e);
//        }
//    }
//
//    private TopicWordResponse toResponse(TopicWord topicWord) {
//        TopicWordResponse response = new TopicWordResponse();
//        response.setId(topicWord.getIdTopicWord());
//        response.setName(topicWord.getNameTopicWord());
//        return response;
//    }
//
//    private WordResponse toWordResponse(Word word) {
//        WordResponse response = new WordResponse();
//        response.setId(word.getIdWord());
//        response.setWord(word.getWord());
//        response.setMean(word.getMean());
//        response.setType(word.getType());
//
//        return response;
//    }

}
