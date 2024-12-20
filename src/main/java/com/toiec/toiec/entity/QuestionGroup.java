package com.toiec.toiec.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class QuestionGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idQuestionGroup;
    private String headerQuestionGroup;
    private String type;
    @OneToMany(mappedBy = "questionGroup", cascade = CascadeType.ALL)
    private List<Question> questions;
}
