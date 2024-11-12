package com.toiec.toiec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idExam;
    private String examName;
    private String status;

}
