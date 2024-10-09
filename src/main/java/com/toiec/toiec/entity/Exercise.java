package com.toiec.toiec.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idExercise;

    @ManyToOne
    @JoinColumn(name = "idLesson")
    private Lesson lesson;

    private String type;
    private String explain;
}
