package com.example.honest_mark_api.service.impl;

import com.example.honest_mark_api.dto.DocumentDto;
import com.example.honest_mark_api.model.Document;
import com.example.honest_mark_api.service.CrptApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class CrptApiServiceServiceImpl implements CrptApiService {
    private final TimeUnit timeUnit;
    private final int requestLimit;
    private final String fileFormat;
    private final AtomicLong lastRequestTime = new AtomicLong(System.currentTimeMillis());
    private final AtomicInteger requestCount = new AtomicInteger(0);
    private final ReentrantLock lock = new ReentrantLock();
    public CrptApiServiceServiceImpl(@Value("${crpt.api.timeunit}") String timeUnitValue,
                                     @Value("${crpt.api.request-limit}") int requestLimit,
                                     @Value ("${crpt.api.file-format}")String fileFormat ) {
        this.timeUnit = TimeUnit.valueOf(timeUnitValue);
        this.requestLimit = requestLimit;
        this.fileFormat = fileFormat;
    }

    @Override
    public Document createDocument(DocumentDto documentDto, String signature) {

        Document document = null;
        long currentTime;
        long timeElapsed;
        lock.lock();
        try {
            currentTime = System.currentTimeMillis();
            timeElapsed = currentTime - lastRequestTime.get();
            if (timeElapsed > timeUnit.toMillis(1)) {
                requestCount.set(0);
                lastRequestTime.set(currentTime);
            }
            if (requestCount.get() >= requestLimit) {
                try {
                    Thread.sleep(timeUnit.toMillis(1));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } else {
                document = signatureAddition(documentDto, signature);
                requestCount.incrementAndGet();
            }
        } finally {
            lock.unlock();
        }
        return document;
    }

    private Document signatureAddition(DocumentDto documentDto, String signature) {
        Document document = Document.builder()
                .description(documentDto.getDocument().getDescription())
                .docId(documentDto.getDocument().getDocId())
                .docStatus(documentDto.getDocument().getDocStatus())
                .docType(documentDto.getDocument().getDocType())
                .importRequest(documentDto.getDocument().isImportRequest())
                .ownerInn(documentDto.getDocument().getOwnerInn())
                .participantInn(documentDto.getDocument().getParticipantInn())
                .producerInn(documentDto.getDocument().getProducerInn())
                .productionDate(documentDto.getDocument().getProductionDate())
                .productionType(documentDto.getDocument().getProductionType())
                .products(documentDto.getDocument().getProducts())
                .regDate(documentDto.getDocument().getRegDate())
                .regNumber(documentDto.getDocument().getRegNumber())
                .signature(signature)
                .build();
        createFile(document);
        return document;
    }


    private File createFile(Document document) {

        String filePath = document.getDocId() + "." + fileFormat;
        File file = new File(filePath);

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            try {
                objectMapper.writeValue(bufferedWriter, document);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }
}
