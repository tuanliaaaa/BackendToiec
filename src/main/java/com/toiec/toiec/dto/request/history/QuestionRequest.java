package com.toiec.toiec.dto.request.history;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionRequest {
    private Integer id;
    private List<Integer> answerList;
    private Boolean isCorrect;
}
