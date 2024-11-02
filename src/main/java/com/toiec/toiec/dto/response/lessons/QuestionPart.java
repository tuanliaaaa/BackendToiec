package com.toiec.toiec.dto.response.lessons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionPart {
    private List<Question> questionList;
    private List<Resource> resourceList;
}
