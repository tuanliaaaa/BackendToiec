package com.toiec.toiec.dto.response.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionGroupResponse {
    private Integer id;
    private String name;
    private List<Question> questionList;
    private List<Resource> resourceList;
}
