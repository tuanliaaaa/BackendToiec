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
    private Integer amountQuestionGroup;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime doneAt;
    private Float score;
    private String username;
    private List<HistoryDetailRequest> historyDetails;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HistoryDetailRequest {
        private Integer idHistoryDetail;
        private Integer score;
        private Integer idLessonDetail;
    }
}
