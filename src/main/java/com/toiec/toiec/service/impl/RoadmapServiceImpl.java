package com.toiec.toiec.service.impl;

import com.toiec.toiec.dto.response.roadmap.GrammarResponse;
import com.toiec.toiec.dto.response.roadmap.RoadmapResponse;
import com.toiec.toiec.dto.response.topicwords.TopicResponse;
import com.toiec.toiec.dto.response.topicwords.WordResponse;
import com.toiec.toiec.entity.LessonDetail;
import com.toiec.toiec.exception.base.NotFoundException;
import com.toiec.toiec.repository.GrammarRepository;
import com.toiec.toiec.repository.RoadmapRepository;
import com.toiec.toiec.service.RoadmapService;
import com.toiec.toiec.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoadmapServiceImpl implements RoadmapService {
    private final RoadmapRepository roadmapRepository;
    private final GrammarRepository grammarRepository;
    @Override
    public List<RoadmapResponse> findAllRoadMap() throws IOException {
        List<RoadmapResponse> responseListResponse = new ArrayList<>();
        List<Object[]> objects = roadmapRepository.findAllRoadmapWithLessonDetail();
        for(Object[] object: objects){
            RoadmapResponse roadmapResponse = new RoadmapResponse();
            roadmapResponse.setId((Integer) object[0]);
            roadmapResponse.setName((String)object[1]);
            if(object[2]!=null){
                List<GrammarResponse> grammarResponseList= JsonUtils.fromJsonList((String)object[2], GrammarResponse.class);
                roadmapResponse.setGrammars(grammarResponseList);
            }
            responseListResponse.add(roadmapResponse);
        }
        return responseListResponse;
    }
    @Override
    public  RoadmapResponse findLessonesByRoodmapId(Integer roadmapId) throws IOException {
        RoadmapResponse roadmapResponse = new RoadmapResponse();
        List<Object[]> objects = roadmapRepository.findRoadmapWithLessonDetail(roadmapId);
        if(objects.size()==0) throw new NotFoundException();
        roadmapResponse.setId(roadmapId);
        roadmapResponse.setName((String)objects.get(0)[1]);
        if(objects.get(0)[2]!=null){
            List<GrammarResponse> grammarResponseList= JsonUtils.fromJsonList((String)objects.get(0)[2], GrammarResponse.class);
            roadmapResponse.setGrammars(grammarResponseList);
        }
        return roadmapResponse;
    }

    @Override
    public GrammarResponse getGrammarById(Integer grammarId)
    {
        LessonDetail lessonDetail=grammarRepository.findById(grammarId).orElseThrow(NotFoundException::new);
        GrammarResponse grammarRepository = new GrammarResponse();
        grammarRepository.setContent(lessonDetail.getContent());
        grammarRepository.setType(lessonDetail.getType());
        grammarRepository.setNameLesson(lessonDetail.getNameLesson());
        grammarRepository.setIdDetail(grammarId);
        return grammarRepository;
    }
}
