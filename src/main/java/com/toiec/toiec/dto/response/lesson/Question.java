package com.toiec.toiec.dto.response.lesson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    private Integer idQuestion;
    private String question;
    private List<Answer> answerList;
}
