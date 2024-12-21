package com.toiec.toiec.service;

import com.toiec.toiec.dto.request.vocabulary.CreateTopicRequest;
import com.toiec.toiec.dto.request.vocabulary.CreateWordRequest;
import com.toiec.toiec.dto.request.vocabulary.TopicUpdatReuqest;
import com.toiec.toiec.dto.response.lesson.QuestionGroupResponse;
import com.toiec.toiec.dto.response.vocabulary.TopicResponse;
import com.toiec.toiec.dto.response.vocabulary.TopicWordResponse;
import com.toiec.toiec.dto.response.vocabulary.WordResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TopicWordService {

    List<TopicWordResponse> getAllTopicWords();
    TopicWordResponse addTopic(CreateTopicRequest request);
    TopicWordResponse editTopic(TopicUpdatReuqest topicUpdatReuqest,Integer topicId);
    TopicResponse getWordsByTopicId(int topicId) throws IOException;
    WordResponse editVocabulary(CreateWordRequest wordRequest,Integer idWord, MultipartFile image, MultipartFile audio);
    void deleteWordById(Integer wordId);
    List<QuestionGroupResponse> getExercisesByTopicId(int topicId) throws IOException;
    WordResponse addVocabulary(CreateWordRequest wordRequest,Integer idTopic, MultipartFile image, MultipartFile audio) ;

}
