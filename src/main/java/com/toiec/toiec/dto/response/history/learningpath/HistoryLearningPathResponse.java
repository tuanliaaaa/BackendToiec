package com.toiec.toiec.dto.response.history.learningpath;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryLearningPathResponse {
    private Integer idHistoryLesson;
    private Boolean isCorrect;
    private String type;
    private Double Score;
    private LocalDateTime createdAt;
    private String username;
    private List<HistoryResponse> historyDetails;
    private Integer idLesson;
    private String lessonName;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HistoryResponse {
        private Integer hdIdHistory;
        private Double score;
        private Integer idLessonDetail;
    }
}

