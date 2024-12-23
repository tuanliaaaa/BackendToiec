package com.toiec.toiec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private History history;
    @ManyToOne
    @JoinColumn(name = "idQuestion")
    private Question question;
    @ManyToOne
    @JoinColumn(name = "idQuestionGroup",nullable = true)
    private QuestionGroup questionGroup;
    @ManyToOne
    @JoinColumn(name = "idLessonDetail",nullable = true)
    private LessonDetail lessonDetail;
}
