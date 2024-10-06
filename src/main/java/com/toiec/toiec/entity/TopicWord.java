package com.toiec.toiec.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class TopicWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTopicWord;

    @Column(nullable = false)
    private String nameTopicWord;

    @OneToMany(mappedBy = "topicWord")
    private List<Word> words;
}

