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
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoadmapServiceImpl implements RoadmapService {
    private final RoadmapRepository roadmapRepository;
    private final GrammarRepository grammarRepository;
    @Override
    public String findAllRoadMap()
    {

        return "D";
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
