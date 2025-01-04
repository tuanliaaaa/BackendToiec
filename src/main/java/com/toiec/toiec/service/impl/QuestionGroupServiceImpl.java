package com.toiec.toiec.service.impl;

import com.toiec.toiec.dto.request.questiongroups.QuestionGroupRequest;
import com.toiec.toiec.dto.response.questiongroup.QuestionGroupResponse;
import com.toiec.toiec.entity.*;
import com.toiec.toiec.exception.base.NotFoundException;
import com.toiec.toiec.repository.*;
import com.toiec.toiec.service.FileStorageService;
import com.toiec.toiec.service.QuestionGroupService;
import com.toiec.toiec.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionGroupServiceImpl implements QuestionGroupService {
    private final QuestionGroupRepository questionGroupRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final ResourceQuestionGroupRepository resourceQuestionGroupRepository;
    private final ResourceRepository resourceRepository;
    private final FileStorageService fileStorageService;

    @Override
    public List<QuestionGroupResponse> searchQuestionGroup(String value,String type,Integer page,Integer size) throws IOException {
        List<Object[]> objects=questionGroupRepository.searchQuestionGroupsByValueQuestionAndType(value,type,(page+1)*size,page*size);
        List<QuestionGroupResponse> questionGroupResponses=new ArrayList<>();
        for(Object[] object:objects)
        {
            QuestionGroupResponse questionGroupResponse=new QuestionGroupResponse();
            questionGroupResponse.setId((Integer)object[0]);
            questionGroupResponse.setName((String)object[1]);
            List<QuestionGroupResponse.Question> questions = JsonUtils.fromJsonList(object[2].toString(),QuestionGroupResponse.Question.class);
            questionGroupResponse.setQuestionList(questions);
            List<QuestionGroupResponse.Resource> resources = JsonUtils.fromJsonList(object[3]!=null?object[3].toString():"[]",QuestionGroupResponse.Resource.class);
            questionGroupResponse.setResourceList(resources);
            questionGroupResponses.add(questionGroupResponse);
        }
        return questionGroupResponses;
    }

    @Override
    public QuestionGroupResponse getQuestionGroupByID(Integer questionGroupId) throws IOException
    {
        Object[] object=questionGroupRepository.getQuestionGroupById(questionGroupId).get(0);
        QuestionGroupResponse questionGroupResponse=new QuestionGroupResponse();
        questionGroupResponse.setId((Integer)object[0]);
        questionGroupResponse.setName((String)object[1]);
        List<QuestionGroupResponse.Question> questions = JsonUtils.fromJsonList(object[2].toString(),QuestionGroupResponse.Question.class);
        questionGroupResponse.setQuestionList(questions);
        List<QuestionGroupResponse.Resource> resources = JsonUtils.fromJsonList(object[3]!=null?object[3].toString():"[]",QuestionGroupResponse.Resource.class);
        questionGroupResponse.setResourceList(resources);
        return questionGroupResponse;
    }
    @Override
    public void createQuestionGroup(QuestionGroupRequest questionGroupRequest, List<MultipartFile> files) {
        String domain="http://127.0.0.1:8080/api/media";
        // Tạo QuestionGroup và lưu thông tin
        QuestionGroup questionGroupNew = new QuestionGroup();
        questionGroupNew.setType(questionGroupRequest.getType());
        questionGroupNew.setHeaderQuestionGroup(questionGroupRequest.getHeaderQuestionGroup());
        questionGroupNew.setExplains(questionGroupRequest.getExplains());
        questionGroupNew.setScript(questionGroupRequest.getScript());
        List<Resource> resourceListNew=new ArrayList<>();
        List<ResourceQuestionGroup> resourceQuestionGroupListNew=new ArrayList<>();
        for (MultipartFile file : files) {
            String contentType = file.getContentType();

            if (contentType != null) {
                if (contentType.startsWith("image/")) {
                    String imageUrl =  fileStorageService.saveFile(file);
                    Resource resourceNew = new Resource();
                    resourceNew.setResoureType("image");
                    resourceNew.setContentResource(domain+"/image/"+imageUrl);
                    resourceListNew.add(resourceNew);
                    ResourceQuestionGroup resourceQuestionGroupNew = new ResourceQuestionGroup();
                    resourceQuestionGroupNew.setQuestionGroup(questionGroupNew);
                    resourceQuestionGroupNew.setResource(resourceNew);
                    resourceQuestionGroupListNew.add(resourceQuestionGroupNew);
                } else if (contentType.startsWith("audio/")) {
                    String audioUrl =  fileStorageService.saveFile(file);
                    Resource resourceNew = new Resource();
                    resourceNew.setContentResource(domain+"/stream/"+audioUrl);
                    resourceNew.setResoureType("audio");
                    resourceListNew.add(resourceNew);
                    ResourceQuestionGroup resourceQuestionGroupNew = new ResourceQuestionGroup();
                    resourceQuestionGroupNew.setQuestionGroup(questionGroupNew);
                    resourceQuestionGroupNew.setResource(resourceNew);
                    resourceQuestionGroupListNew.add(resourceQuestionGroupNew);
                }
            }
        }

        // Lưu các câu hỏi và câu trả lời
        List<Question> questionListNew = new ArrayList<>();
        List<Answer> answerListNew = new ArrayList<>();
        for (QuestionGroupRequest.QuestionRequest questionRequest : questionGroupRequest.getQuestionList()) {
            Question questionNew = new Question();
            questionNew.setQuestionGroup(questionGroupNew);
            questionNew.setScript(questionRequest.getScript());
            questionNew.setValue(questionRequest.getValue());
            questionNew.setType(questionGroupRequest.getType());
            questionNew.setExplanation(questionNew.getExplanation());
            questionListNew.add(questionNew);
            // Lưu các câu trả lời của câu hỏi
            for (QuestionGroupRequest.QuestionRequest.AnswerRequest answerRequest : questionRequest.getAnswerList()) {
                Answer answer = new Answer();
                answer.setCorrect(answerRequest.getIsCorrect());
                answer.setQuestion(questionNew);
                answer.setValue(answerRequest.getValue());
                answerListNew.add(answer);
            }

        }

        // Lưu QuestionGroup vào cơ sở dữ liệu
        questionGroupNew = questionGroupRepository.save(questionGroupNew);
        questionListNew = questionRepository.saveAll(questionListNew);
        answerListNew = answerRepository.saveAll(answerListNew);
        resourceListNew = resourceRepository.saveAll(resourceListNew);
        resourceQuestionGroupListNew = resourceQuestionGroupRepository.saveAll(resourceQuestionGroupListNew);
    }


}
