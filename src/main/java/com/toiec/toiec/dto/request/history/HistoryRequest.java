package com.toiec.toiec.dto.request.history;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class HistoryRequest {
    private String type;
    private Integer amountQuestionGroup;
    private List<QuestionRequest> questionList;
}
