package com.toiec.toiec.entity;

import com.toiec.toiec.dto.request.vocabulary.CreateWordRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class LessonDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idLessonDetail;
    private String nameLesson;
    private String type;
    @Lob
    private String content;
    private String image;
    private String audio;
    private String transcription;
    private String example;
    private String partOfSpeech;
    @ManyToOne
    @JoinColumn(name = "idLesson")
    private Lesson lesson;
    public LessonDetail(CreateWordRequest createWordRequest,String image,String audio)
    {
        this.nameLesson = createWordRequest.getNameLesson();
        this.example = createWordRequest.getExample();
        this.partOfSpeech = createWordRequest.getPartOfSpeech();
        this.transcription = createWordRequest.getTranscription();
        this.content = createWordRequest.getContent();
        this.type = "vocabulary";
        this.image = image;
        this.audio = audio;

    }
}
