package com.toiec.toiec.dto.response.history;

import com.toiec.toiec.entity.History;
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
    private Integer idHistory;
    private Integer user;
    private Lesson lesson;
    private String type;
    private LocalDateTime createdAt;
    private LocalDateTime doneAt;
    private String status;
    private Float score;
    private Integer amountQuestionGroup;
    public HistoryResponse(History history){
        this.idHistory = history.getIdHistory();
        this.user = history.getUser().getIdUser();
        this.lesson = history.getLesson();
        this.type = history.getType();
        this.createdAt = history.getCreatedAt();
        this.status = history.getStatus();
        this.amountQuestionGroup = history.getAmountQuestionGroup();
        this.score = history.getScore();
        this.doneAt = history.getDoneAt();
    }
}
