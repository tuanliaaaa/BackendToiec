package com.toiec.toiec.dto.response.questiongroup;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionGroupResponse {
    private Integer id;
    private String name;
    private String type;
    private List<Question> questionList;
    private List<Resource> resourceList;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Question {
        private Integer idQuestion;
        private String question;
        private List<Answer> answerList;
        private String explanation;
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Answer {
            private Integer idAnswer;
            private String answer;
            private Boolean isCorrect;
        }

    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Resource {
        private  Integer idResource;
        private  String resourceContent;
        private  String resourceType;
    }

}
