package com.toiec.toiec.dto.response.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamPartDetailResponse {
    private Integer idExam;
    private String examName;
    private List<QuestionGroupResponse> questionGroups;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class QuestionGroupResponse {
        private Integer id;
        private String name;
        private List<Question> questionList;
        private List<Resource> resourceList;
    }

}
