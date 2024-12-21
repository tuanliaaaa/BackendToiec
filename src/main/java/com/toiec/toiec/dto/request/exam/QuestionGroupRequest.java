package com.toiec.toiec.dto.request.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionGroupRequest {
    private Integer idQuestionGroup;
    private Integer orderOfQuestionGroup;
}
