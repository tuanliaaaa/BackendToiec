package com.toiec.toiec.service;

import com.toiec.toiec.dto.response.roadmap.GrammarResponse;
import com.toiec.toiec.dto.response.roadmap.RoadmapResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface RoadmapService {
    List<RoadmapResponse> findAllRoadMap() throws IOException;
    GrammarResponse getGrammarById(Integer id);
    RoadmapResponse findLessonesByRoodmapId(Integer roodmapId) throws IOException;
}
