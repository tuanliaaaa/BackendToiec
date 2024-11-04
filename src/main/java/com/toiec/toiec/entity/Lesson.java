package com.toiec.toiec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idLesson;

    private String nameLesson;
    private String type;
    private String content;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private String theory;
}
