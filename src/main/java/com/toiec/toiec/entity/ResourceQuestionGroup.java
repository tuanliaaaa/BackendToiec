package com.toiec.toiec.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ResourceQuestionGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idResourceQuestion;
    @ManyToOne
    @JoinColumn(name = "idResource", nullable = false)
    private Resource resource;
    @ManyToOne
    @JoinColumn(name = "idQuestionGroup", nullable = false)
    private  QuestionGroup questionGroup;
}
