package com.example.honest_mark_api.controller;

import com.example.honest_mark_api.dto.DocumentDto;
import com.example.honest_mark_api.model.Document;
import com.example.honest_mark_api.service.CrptApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ismp.crpt.ru/api/v3/lk/")
@RequiredArgsConstructor
public class CrptApiController {
    private final CrptApiService crptApiService;

    @PostMapping("/documents/create")
    public ResponseEntity<Document> createDocument(@RequestBody DocumentDto document, @RequestParam String signature) {
        Document createdDocument = crptApiService.createDocument(document, signature);
        return ResponseEntity.ok(createdDocument);
    }
}
