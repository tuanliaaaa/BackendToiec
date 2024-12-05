package com.toiec.toiec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class LessonDetailQuestionGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idLessonDetailQuestionGroup;

    @ManyToOne
    @JoinColumn(name = "idLessonDetail")
    private LessonDetail lessonDetail;

    @ManyToOne
    @JoinColumn(name = "idQuestionGroup")
    private QuestionGroup questionGroup;

}
