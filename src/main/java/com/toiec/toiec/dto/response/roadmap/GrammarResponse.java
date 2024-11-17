package com.toiec.toiec.dto.response.roadmap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrammarResponse {
    private Integer idDetail;
    private String content;
    private String nameLesson;
    private String type;
}
