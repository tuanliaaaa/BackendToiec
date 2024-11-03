package com.toiec.toiec.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data

public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idLesson;

    private String nameLesson;
    private String type;
    private String content;
    @Column(name = "theory", columnDefinition = "json")
    private String theory;
}
