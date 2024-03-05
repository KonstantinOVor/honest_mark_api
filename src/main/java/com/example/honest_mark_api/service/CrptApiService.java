package com.example.honest_mark_api.service;

import com.example.honest_mark_api.dto.DocumentDto;
import com.example.honest_mark_api.model.Document;
import org.springframework.http.HttpEntity;

public interface CrptApiService {
   Document createDocument(DocumentDto document, String signature);
}
