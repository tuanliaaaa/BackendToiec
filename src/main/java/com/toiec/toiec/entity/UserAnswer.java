package com.toiec.toiec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUserAnswer;
    @ManyToOne
    @JoinColumn(name = "idAnswer")
    private Answer answer;
    @ManyToOne
    @JoinColumn(name = "idHistoryDetail")
    private HistoryDetail historyDetail;
    public UserAnswer( Answer answer, HistoryDetail historyDetail) {
        this.answer = answer;
        this.historyDetail = historyDetail;
    }
}
