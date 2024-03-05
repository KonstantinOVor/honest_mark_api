package com.example.honest_mark_api.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Builder
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(mappedBy = "document")
    private Description description;
    private String docId;
    private String docStatus;
    private String docType;
    private boolean importRequest;
    private String ownerInn;
    private String participantInn;
    private String producerInn;
    private LocalDate productionDate;
    private String productionType;
    @OneToMany(mappedBy = "document")
    private List<Product> products;
    private String regDate;
    private String regNumber;
    private String signature;
}
