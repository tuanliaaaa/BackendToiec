package com.toiec.toiec.service.impl;

import com.toiec.toiec.dto.response.topicwords.TopicWordResponse;
import com.toiec.toiec.dto.response.topicwords.WordResponse;
import com.toiec.toiec.entity.Lesson;
import com.toiec.toiec.repository.TopicRepository;
import com.toiec.toiec.repository.WordRepository;
import com.toiec.toiec.service.TopicWordService;
import com.toiec.toiec.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TopicWordServiceImpl implements TopicWordService {
    private final TopicRepository topicRepository;
    private final WordRepository wordRepository;

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
    public List<WordResponse> getWordsByTopicId(int topicId) {
        List<Lesson> lessonList= wordRepository.findByTopicWordId(topicId);
        try{
            System.out.println(lessonList.get(0).getTheory());
            return JsonUtils.fromJsonList(lessonList.get(0).getTheory(), WordResponse.class);
        }catch (Exception e){
            return null;
        }
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
