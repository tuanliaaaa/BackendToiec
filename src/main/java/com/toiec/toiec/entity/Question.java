package com.toiec.toiec.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idQuestion;

    private int value;

    @ManyToOne
    @JoinColumn(name = "idExercise")
    private Exercise exercise;

    private boolean isTrue;
}
