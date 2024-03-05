package com.example.honest_mark_api.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Description {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String participantInn;
    @OneToOne
    private Document document;
}
