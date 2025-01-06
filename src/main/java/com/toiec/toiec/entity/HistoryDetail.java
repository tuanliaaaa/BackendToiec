package com.toiec.toiec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHistoryDetail;
    private Boolean isCorrect;
    private String type;
    private Integer Score;
    @ManyToOne
    @JoinColumn(name = "idHistory")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private History history;
    @ManyToOne
    @JoinColumn(name = "idQuestion")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Question question;
    @ManyToOne
    @JoinColumn(name = "idQuestionGroup",nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private QuestionGroup questionGroup;
    @ManyToOne
    @JoinColumn(name = "idLessonDetail",nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private LessonDetail lessonDetail;
}
