package com.toiec.toiec.dto.request.questiongroups;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionGroupRequest {
    private String headerQuestionGroup;
    private String type;
    private String explains;
    private String script;
    private List<QuestionRequest> questionList;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuestionRequest {
        private String value;
        private String script;
        private String explanation;
        private List<AnswerRequest> answerList;
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class AnswerRequest {
            private String value;
            private Boolean isCorrect;
        }

    }

}
