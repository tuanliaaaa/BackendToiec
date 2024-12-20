package com.toiec.toiec.service;

import com.toiec.toiec.dto.request.roodmap.CreateDay;
import com.toiec.toiec.dto.request.roodmap.CreateGrammar;
import com.toiec.toiec.dto.request.roodmap.UpdateDay;
import com.toiec.toiec.dto.request.roodmap.UpdateGrammar;
import com.toiec.toiec.dto.response.roadmap.DayResponse;
import com.toiec.toiec.dto.response.roadmap.GrammarResponse;
import com.toiec.toiec.dto.response.roadmap.RoadmapResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface RoadmapService {
    List<GrammarResponse> findAllLessonDetail(Pageable pageable) throws IOException;
    DayResponse updateDay(UpdateDay updateDay,Integer dayId);
    RoadmapResponse createdDay(CreateDay createDay);
    void deleteDay(Integer dayId);
    List<RoadmapResponse> findAllRoadMap() throws IOException;
    GrammarResponse getGrammarById(Integer id);
    RoadmapResponse findLessonesByRoodmapId(Integer roodmapId) throws IOException;
    List<GrammarResponse> findLessonesByNameLesson(String nameLesson, Pageable pageable) ;
    RoadmapResponse updateLessonForDay(List<Integer> lsLessonDetail,Integer dayId) throws IOException;
    GrammarResponse createGrammar(CreateGrammar createGrammar, MultipartFile word);
    GrammarResponse updateGrammar(Integer idGrammar,UpdateGrammar grammar,MultipartFile file);

}
