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
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHistory;
    @ManyToOne
    @JoinColumn(name = "idUser", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "idExam", nullable = true)
    private Exam exam;
    @ManyToOne
    @JoinColumn(name = "idLesson", nullable = true)
    private Lesson lesson;
    private String type;
    private Integer amountQuestionGroup;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime doneAt;
    private Float score;

}
