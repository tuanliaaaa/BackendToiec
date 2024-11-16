package com.toiec.toiec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
    private String content;
    private String image;
    private String audio;
    private String transcription;
    private String example;
    private String partOfSpeech;
    @ManyToOne
    @JoinColumn(name = "idLesson")
    private Lesson lesson;
}
