package com.toiec.toiec.dto.request.history;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryTopicRequest {
    private Integer topicId;
    List<HistoryWordRequest> words;
}
