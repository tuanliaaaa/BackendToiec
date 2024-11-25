package com.toiec.toiec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idChat;
    @Lob
    private String mess;
    private String type;
    @ManyToOne
    @JoinColumn(name = "idSender",nullable = true)
    private User sender;
    @ManyToOne
    @JoinColumn(name = "idReceived",nullable = true)
    private User received;
    private LocalDateTime date;
}
