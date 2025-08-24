package com.haoge.spring_ai_learn.controller;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmbeddingControllerTest {

    @Resource
    EmbeddingController embeddingController;

    @Test
    void embedString() {
        embeddingController.embedString();
    }

    @Test
    void embedDocument() {
    }

    @Test
    void embedList() {
    }

    @Test
    void embed() {
    }

    @Test
    void store() {
    }

    @Test
    void search() {
        List<Document> search = embeddingController.search("皇帝");
    }

    @Test
    void searchTop() {
    }
}