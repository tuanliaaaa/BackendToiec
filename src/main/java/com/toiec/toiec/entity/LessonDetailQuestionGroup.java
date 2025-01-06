package com.toiec.toiec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private LessonDetail lessonDetail;

    @ManyToOne
    @JoinColumn(name = "idQuestionGroup")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private QuestionGroup questionGroup;

}
