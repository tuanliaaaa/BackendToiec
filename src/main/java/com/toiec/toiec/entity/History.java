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
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHistory;
    @ManyToOne
    @JoinColumn(name = "idUser", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
    @ManyToOne
    @JoinColumn(name = "idExam", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "idHistoryLesson", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private HistoryLesson historyLesson;
    @ManyToOne
    @JoinColumn(name = "idLessonDetail",nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private LessonDetail lessonDetail;
    private String type;
    private Integer amountQuestionGroup;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime doneAt;
    private Float score;

}
