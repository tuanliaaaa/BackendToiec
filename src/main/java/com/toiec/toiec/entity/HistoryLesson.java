package com.toiec.toiec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryLesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHistoryLesson;
    private Boolean isCorrect;
    private String type;
    private Integer Score;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private User user;
    @ManyToOne
    @JoinColumn(name = "idLesson")
    private Lesson lesson;

    private LocalDateTime createdAt;
}
