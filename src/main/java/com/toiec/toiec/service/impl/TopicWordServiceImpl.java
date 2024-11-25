package com.toiec.toiec.service.impl;

import com.toiec.toiec.dto.request.topicwords.CreateWordRequest;
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
import com.toiec.toiec.exception.word.WordNotFoundException;
import com.toiec.toiec.repository.QuestionGroupRepository;
import com.toiec.toiec.repository.TopicRepository;
import com.toiec.toiec.repository.WordRepository;
import com.toiec.toiec.service.FileStorageService;
import com.toiec.toiec.service.TopicWordService;
import com.toiec.toiec.utils.JsonUtils;
import com.toiec.toiec.utils.MapperUtils;
import com.toiec.toiec.utils.UpdateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TopicWordServiceImpl implements TopicWordService {
    private final TopicRepository topicRepository;
    private final FileStorageService fileStorageService;
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
    @Override
    public void deleteWordById(Integer wordId)
    {
        LessonDetail word = wordRepository.findById(wordId).orElseThrow(
                WordNotFoundException::new
        );
        Path filePath = Paths.get("media/", word.getImage().substring(("http://127.0.0.1:8080/api/media/image/").length()-1));
        File file = filePath.toFile();
        if (file.exists()) {
            boolean deleted = file.delete();
        }
        filePath = Paths.get("media/", word.getAudio().substring(("http://127.0.0.1:8080/api/media/stream/").length()-1));
        file = filePath.toFile();
        if (file.exists()) {
            boolean deleted = file.delete();
        }
        wordRepository.deleteById(wordId);
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
        topicResponse.setWords(new ArrayList<>());
        if(objects.get(0)[3]!=null){
            List<WordResponse> questionList= JsonUtils.fromJsonList((String)objects.get(0)[3],WordResponse.class);
            if(questionList.get(0).getIdDetail()!=null) topicResponse.setWords(questionList);
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
    @Override
    public WordResponse addVocabulary(CreateWordRequest wordRequest,Integer idTopic, MultipartFile image, MultipartFile audio) {
        Lesson topic = topicRepository.findById(idTopic).orElseThrow(
                NotFoundException::new
        );
        String imageUrl = image != null ? fileStorageService.saveFile(image) : null;
        String audioUrl = audio != null ? fileStorageService.saveFile(audio) : null;

        String domain="http://127.0.0.1:8080/api/media";
        LessonDetail newWord = new LessonDetail(wordRequest,domain+"/image/"+imageUrl,domain+"/stream/"+audioUrl);
        newWord.setLesson(topic);
        newWord=wordRepository.save(newWord);
        return MapperUtils.toDTO(newWord,WordResponse.class);
    }
    @Override
    public WordResponse editVocabulary(CreateWordRequest wordRequest,Integer idWord, MultipartFile image, MultipartFile audio) {
        LessonDetail word= wordRepository.findById(idWord).orElseThrow(NotFoundException::new);
        String imageUrl = image != null ? fileStorageService.saveFile(image) : null;
        String audioUrl = audio != null ? fileStorageService.saveFile(audio) : null;
        if(imageUrl==null)word.setImage(imageUrl);
        if(audioUrl==null)word.setAudio(audioUrl);
        String domain="http://127.0.0.1:8080/api/media/stream/";
        if (wordRequest!=null)UpdateUtils.updateEntityFromDTO(word,wordRequest);

        word=wordRepository.save(word);
        return MapperUtils.toDTO(word,WordResponse.class);
    }

}
