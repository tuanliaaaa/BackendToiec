package com.toiec.toiec.entity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRole;

    @Column(nullable = false)
    private String roleName;

    private String status;

}
