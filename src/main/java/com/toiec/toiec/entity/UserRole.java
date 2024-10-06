package com.toiec.toiec.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUserRole;

    @ManyToOne
    @JoinColumn(name = "idUser", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "idRole", nullable = false)
    private Role role;
}
