package com.toiec.toiec.dto.response.topicwords;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicResponse {
    private int id;
    private String name;
    private List<WordResponse> words;
}
