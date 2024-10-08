package com.toiec.toiec.service;

import com.toiec.toiec.dto.request.topicwords.CreateTopicWordRequest;
import com.toiec.toiec.dto.request.topicwords.CreateWordRequest;
import com.toiec.toiec.dto.response.topicwords.TopicWordResponse;
import com.toiec.toiec.dto.response.topicwords.WordResponse;

import java.util.List;

public interface TopicWordService {

    List<TopicWordResponse> getAllTopicWords();
    TopicWordResponse addTopicWord(CreateTopicWordRequest request);
    List<WordResponse> getWordsByTopicId(int topicId);
    WordResponse addWordToTopic(int topicId, CreateWordRequest request);
}
