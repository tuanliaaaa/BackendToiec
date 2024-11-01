package com.toiec.toiec.dto.response.lessons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Answer {
    private Integer idAnswer;
    private String answer;
    private Boolean isCorrect;
}