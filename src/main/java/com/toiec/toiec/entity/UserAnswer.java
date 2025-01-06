package com.toiec.toiec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Answer answer;
    @ManyToOne
    @JoinColumn(name = "idHistoryDetail")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private HistoryDetail historyDetail;
    public UserAnswer( Answer answer, HistoryDetail historyDetail) {
        this.answer = answer;
        this.historyDetail = historyDetail;
    }
}
