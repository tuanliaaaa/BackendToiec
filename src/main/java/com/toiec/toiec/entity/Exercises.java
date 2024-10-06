package com.toiec.toiec.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Exercises {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idExercises;

    @Column(nullable = false)
    private String header;

    @ManyToOne
    @JoinColumn(name = "idWord", nullable = false)
    private Word word;

    @Column(nullable = false)
    private int type;
}
