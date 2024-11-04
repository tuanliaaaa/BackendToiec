package com.toiec.toiec.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idResource;
    private String resoureType;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String contentResource;
}
