package com.toiec.toiec.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Histories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idHistories;

    @ManyToOne
    @JoinColumn(name = "idAccount")
    private User user;

    @ManyToOne
    @JoinColumn(name = "idLesson",nullable = true)
    private Lesson lesson;

}
