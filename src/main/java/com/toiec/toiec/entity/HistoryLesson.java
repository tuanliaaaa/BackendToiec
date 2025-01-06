package com.toiec.toiec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    private Float Score;

    @ManyToOne
    @JoinColumn(name = "idUser")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
    @ManyToOne
    @JoinColumn(name = "idLesson")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Lesson lesson;

    private LocalDateTime createdAt;
}
