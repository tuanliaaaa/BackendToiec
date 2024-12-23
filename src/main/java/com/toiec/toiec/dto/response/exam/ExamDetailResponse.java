package com.toiec.toiec.dto.response.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamDetailResponse {
    private Integer idExam;
    private String examName;
    private List<PartResponse> partResponseList;
}
