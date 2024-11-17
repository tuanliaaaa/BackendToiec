package com.toiec.toiec.dto.response.roadmap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoadmapResponse {
    private Integer id;
    private String name;
    private List<GrammarResponse> grammars;
}
