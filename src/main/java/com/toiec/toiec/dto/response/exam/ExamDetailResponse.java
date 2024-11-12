package com.toiec.toiec.dto.response.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamDetailResponse {
    private Integer idExam;
    private String examName;
    private String status;
}
