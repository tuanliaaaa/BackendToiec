package com.toiec.toiec.dto.response.vocabulary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordResponse {
    private Integer idDetail;
    private String image;
    private String example;
    private String content;
    private String type;
    private String audio;
    private String nameLesson;
    private String transcription;
    private String partOfSpeech;

}