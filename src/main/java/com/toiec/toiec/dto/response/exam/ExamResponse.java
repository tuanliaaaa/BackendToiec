package com.toiec.toiec.dto.response.exam;

import com.toiec.toiec.entity.QuestionGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamResponse {
    private Integer idExam;
    private String examName;
    private String status;
    private List<QuestionGroup> orders;
}
