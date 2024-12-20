package com.toiec.toiec.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class QuestionGroupExam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idQuestionGroupExam;
    @ManyToOne
    @JoinColumn(name = "idExam")
    private Exam exam;
    @ManyToOne
    @JoinColumn(name = "idQuestionGroup")
    private QuestionGroup questionGroup;
    private Integer orderOfQuestionGroup;
}
