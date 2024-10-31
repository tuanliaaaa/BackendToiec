package com.toiec.toiec.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ResourceQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idResourceQuestion;
    @ManyToOne
    @JoinColumn(name = "idResource", nullable = false)
    private Resource resource;
    @ManyToOne
    @JoinColumn(name = "idQuestion", nullable = false)
    private  Question question;
}
