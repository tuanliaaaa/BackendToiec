package com.toiec.toiec.dto.response.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartResponse {
    private String part;
    private List<QuestionGroupResponse> questionGroups;
}
