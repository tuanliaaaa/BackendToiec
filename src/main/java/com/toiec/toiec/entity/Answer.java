package com.toiec.toiec.entity;

import jakarta.persistence.*;

@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String value;
    private Boolean correct;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}
