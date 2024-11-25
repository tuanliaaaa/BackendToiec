package com.toiec.toiec.service;

import com.toiec.toiec.dto.request.topicwords.CreateWordRequest;
import com.toiec.toiec.dto.response.lessons.QuestionGroupResponse;
import com.toiec.toiec.dto.response.topicwords.TopicResponse;
import com.toiec.toiec.dto.response.topicwords.TopicWordResponse;
import com.toiec.toiec.dto.response.topicwords.WordResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TopicWordService {

    List<TopicWordResponse> getAllTopicWords();
//    TopicWordResponse addTopicWord(CreateTopicWordRequest request);
    TopicResponse getWordsByTopicId(int topicId) throws IOException;
    WordResponse editVocabulary(CreateWordRequest wordRequest,Integer idWord, MultipartFile image, MultipartFile audio);
    void deleteWordById(Integer wordId);
    List<QuestionGroupResponse> getExercisesByTopicId(int topicId) throws IOException;
    WordResponse addVocabulary(CreateWordRequest wordRequest,Integer idTopic, MultipartFile image, MultipartFile audio) ;

}
