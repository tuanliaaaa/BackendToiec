package com.toiec.toiec.dto.request.topicwords;

import lombok.Data;

@Data
public class CreateWordRequest {
    private String nameLesson;
    private String type;
    private String content;
    private String audio;
    private String transcription;
    private String example;
    private String partOfSpeech;

}
