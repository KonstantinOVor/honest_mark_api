package com.example.honest_mark_api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "document_id", referencedColumnName = "id")
    private Document document;
    private String certificateDocument;
    private LocalDate certificateDocumentDate;
    private String certificateDocumentNumber;
    private String ownerInn;
    private String producerInn;
    private LocalDate productionDate;
    private String tnvedCode;
    private String uitCode;
    private String uituCode;

}
