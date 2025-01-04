package com.toiec.toiec.dto.response.history.lessonbypart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryDetailLessonByPartResponse {

    private Integer idHistory;
    private Integer amountQuestionGroup;
    private LocalDateTime createdAt;
    private LocalDateTime doneAt;
    private String type;
    private Float score;
    private List<QuestionGroupResponse> questionGroups;

    public HistoryDetailLessonByPartResponse(Object[] object) {
        this.idHistory = (Integer) object[0];
        this.amountQuestionGroup = (Integer) object[1];
        this.createdAt = ((Timestamp) object[2]).toLocalDateTime();
        this.doneAt = ((Timestamp) object[3]).toLocalDateTime();
        this.type = (String) object[4];
        this.score = (Float) object[5];
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class QuestionGroupResponse {
        private Integer idQuestionGroup;
        private String questionGroupName;
        private List<Question> questionList;
        private List<Resource> resourceList;

        @AllArgsConstructor
        @NoArgsConstructor
        @Data
        public static class Question {
            private Integer idQuestion;
            private String question;
            private List<Answer> answerList;
            private Boolean isCorrect;

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            public static class Answer {
                private Integer idAnswer;
                private String answer;
                private Boolean isCorrect;
                private Boolean isUserSelect;
            }
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Resource {
            private Integer idResource;
            private String resourceContent;
            private String resourceType;
        }
    }

}
