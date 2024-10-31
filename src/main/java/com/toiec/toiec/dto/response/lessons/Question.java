package com.toiec.toiec.dto.response.lessons;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Question {
    private Integer idQuestion;
    private String question;
    private List<Answer> answerList;
    private List<Resource> resourceList;
}
