package com.toiec.toiec.dto.response.history.vocabulary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryVocabularyResponse {

    private Integer idHistory;
    private String type;
    private LocalDateTime createdAt;
    private Double score;
    private String username;
    private List<HistoryDetailResponse> historyDetails;
    private Integer idLesson;
    private String lessonName;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HistoryDetailResponse {
        private Integer hdIdHistory;
        private Integer score;
        private Integer idLessonDetail;
    }
}
