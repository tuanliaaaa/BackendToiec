package com.toiec.toiec.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idWord;


    @ManyToOne
    @JoinColumn(name = "idTopicWord", nullable = false)
    private TopicWord topicWord;

    @Column(nullable = false)
    private String word;

    @Column(nullable = false)
    private String mean;

    @Column(nullable = false)
    private String type;

    @OneToMany(mappedBy = "word")
    private List<Exercises> exercises;
}

