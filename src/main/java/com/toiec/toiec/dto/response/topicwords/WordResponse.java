package com.toiec.toiec.dto.response.topicwords;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordResponse {
    private String word;
    private String pronunciation;
    private String part_of_speech;
    private String definition;
    private String example_sentence;
    private String audio;
}