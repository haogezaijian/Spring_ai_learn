package com.haoge.spring_ai_learn.config;

import com.haoge.spring_ai_learn.rag.DocumentLoader;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class VectorStoreConfig {

    @Resource
    private DocumentLoader documentLoader;

    @Bean
    public VectorStore vectorStore(OpenAiEmbeddingModel embeddingModel) {
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build();
        List<Document> documents = documentLoader.loadDocuments();
        vectorStore.add(documents);
        return vectorStore;
    }
}
