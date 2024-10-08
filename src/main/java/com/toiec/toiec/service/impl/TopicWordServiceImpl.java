package com.toiec.toiec.service.impl;

import com.toiec.toiec.dto.request.topicwords.CreateTopicWordRequest;
import com.toiec.toiec.dto.request.topicwords.CreateWordRequest;
import com.toiec.toiec.dto.response.topicwords.TopicWordResponse;
import com.toiec.toiec.dto.response.topicwords.WordResponse;
import com.toiec.toiec.entity.TopicWord;
import com.toiec.toiec.entity.Word;
import com.toiec.toiec.repository.TopicWordRepository;
import com.toiec.toiec.repository.WordRepository;
import com.toiec.toiec.service.TopicWordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TopicWordServiceImpl implements TopicWordService {
    private final TopicWordRepository topicWordRepository;
    private final WordRepository wordRepository;


    @Override
    public List<TopicWordResponse> getAllTopicWords() {
        try {
            return topicWordRepository.findAll()
                    .stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error get all topic", e);
        }
    }

    @Override
    public TopicWordResponse addTopicWord(CreateTopicWordRequest request) {
        try {
            TopicWord topicWord = new TopicWord();
            topicWord.setNameTopicWord(request.getName());
            topicWord = topicWordRepository.save(topicWord);
            return toResponse(topicWord);
        } catch (Exception e) {
            throw new RuntimeException("Error creating new topic word", e);
        }
    }

    @Override
    public List<WordResponse> getWordsByTopicId(int topicId) {
        try {
            return wordRepository.findByTopicWordId(topicId)
                    .stream()
                    .map(this::toWordResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error get topic by id: " + topicId, e);
        }
    }

    @Override
    public WordResponse addWordToTopic(int topicId, CreateWordRequest request) {
        try {
            TopicWord topicWord = topicWordRepository.findById(topicId)
                    .orElseThrow(() -> new RuntimeException("Topic not found"));
            Word word = new Word();
            word.setTopicWord(topicWord);
            word.setWord(request.getWord());
            word.setMean(request.getMean());
            word.setType(request.getType());
            word = wordRepository.save(word);
            return toWordResponse(word);
        } catch (Exception e) {
            throw new RuntimeException("Error adding word to topic with id: " + topicId, e);
        }
    }

    private TopicWordResponse toResponse(TopicWord topicWord) {
        TopicWordResponse response = new TopicWordResponse();
        response.setId(topicWord.getIdTopicWord());
        response.setName(topicWord.getNameTopicWord());
        return response;
    }

    private WordResponse toWordResponse(Word word) {
        WordResponse response = new WordResponse();
        response.setId(word.getIdWord());
        response.setWord(word.getWord());
        response.setMean(word.getMean());
        response.setType(word.getType());

        return response;
    }
}
