package com.toiec.toiec.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRefreshToken;

    @ManyToOne
    @JoinColumn(name = "idUser", nullable = false)
    private User user;
    private String refreshToken;

    @Column(nullable = false)
    private LocalDateTime expiration;
}

