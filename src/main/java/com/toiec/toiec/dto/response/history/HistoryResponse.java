package com.toiec.toiec.dto.response.history;

import com.toiec.toiec.entity.Histories;
import com.toiec.toiec.entity.Lesson;
import com.toiec.toiec.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryResponse {
    private Integer idHistories;
    private Integer user;
    private Lesson lesson;
    private String type;
    private LocalDateTime date;
    private String status;
    private Integer correctQuestion;
    private Integer amountQuestion;
    public HistoryResponse(Histories histories){
        this.idHistories = histories.getIdHistories();
        this.user = histories.getUser().getIdUser();
        this.lesson = histories.getLesson();
        this.type = histories.getType();
        this.date = histories.getDate();
        this.status = histories.getStatus();
        this.correctQuestion = histories.getCorrectQuestion();
        this.amountQuestion = histories.getAmountQuestion();
    }
}
