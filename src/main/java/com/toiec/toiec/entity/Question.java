package com.toiec.toiec.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "question", indexes = {
        @Index(name = "idx_question_type", columnList = "type")
})
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idQuestion;

    private String value;


    private String type;
    private String explanation;
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> answers;


    @ManyToOne
    @JoinColumn(name = "idQuestionGroup", nullable = false)
    private QuestionGroup questionGroup;
}
