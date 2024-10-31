package com.toiec.toiec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHistoryDetail;
    @ManyToOne
    @JoinColumn(name = "idHistories", nullable = true)
    private Histories history;
    @ManyToOne
    @JoinColumn(name = "idQuestion", nullable = false)
    private Question question;
    private String answerSeleted;
}
