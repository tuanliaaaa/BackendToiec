package com.toiec.toiec.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
public class QuestionGroupExam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idQuestionGroupExam;
    @ManyToOne
    @JoinColumn(name = "idExam")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Exam exam;
    @ManyToOne
    @JoinColumn(name = "idQuestionGroup")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private QuestionGroup questionGroup;
    private Integer orderOfQuestionGroup;
}
