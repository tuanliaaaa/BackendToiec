package com.toiec.toiec.dto.request.history.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@Data
@NoArgsConstructor
public class CreateExamUser {
    private List<QuestionRequest> questionList;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuestionRequest{
        private Integer id;
        private List<Integer> answerList;
    }
}
