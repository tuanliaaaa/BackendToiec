package com.toiec.toiec.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "histories", indexes = {
        @Index(name = "idx_history_type", columnList = "type")
})
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

    private String type;
    private LocalDateTime date;
    private String status;
    private Integer correctQuestion;
    private Integer amountQuestion;
}
