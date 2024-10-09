package com.toiec.toiec.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Theory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTheory;

    @ManyToOne
    @JoinColumn(name = "idLesson")
    private Lesson lesson;

    private String content;
}
