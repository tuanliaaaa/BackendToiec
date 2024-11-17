package com.toiec.toiec.dto.request.topicwords;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateWordRequest {
    private String nameLesson;
    private String content;
    private String transcription;
    private String example;
    private String partOfSpeech;

}
