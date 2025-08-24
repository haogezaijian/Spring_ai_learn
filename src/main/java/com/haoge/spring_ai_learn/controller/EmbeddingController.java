package com.haoge.spring_ai_learn.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/embedding")
@Slf4j
public class EmbeddingController {

    private final EmbeddingModel embeddingModel;

    public EmbeddingController(OpenAiEmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @Resource
    private VectorStore vectorStore;

    @GetMapping("/embed/string")
    public void embedString(){
        float[] embed = embeddingModel.embed("hello word");
        log.info("vector: {}", embed);
        log.info("length: {}", embed.length);
    }

    @GetMapping("/embed/document")
    public void embedDocument(){
        float[] embed = embeddingModel.embed(new Document("hello world"));
        log.info("vector: {}", embed);
        log.info("length: {}", embed.length);
    }

    @GetMapping("/embed/list")
    public void embedList(){
        List<float[]> embed = embeddingModel.embed(Arrays.asList("hello world", "hello world"));
        log.info("size: {}", embed.size());
        for (float[] floats : embed) {
            log.info("vector: {}", floats);
            log.info("length: {}", floats.length);
        }
    }

    @GetMapping("/embed")
    public void embed(){
        OpenAiEmbeddingOptions options = new OpenAiEmbeddingOptions();
        options.setDimensions(2048);

        EmbeddingRequest embeddingRequest = new EmbeddingRequest(List.of("hello world"), options);
        float[] output = embeddingModel.call(embeddingRequest).getResult().getOutput();
        log.info("embedding: {}", output);
        log.info("embedding: {}", output.length);
    }

    @GetMapping("/store")
    public void store(){
        // 1. 为每个文档创建元数据（键值对形式）
        Map<String, Object> liShiMinMeta = new HashMap<>();
        liShiMinMeta.put("person", "李世民");
        liShiMinMeta.put("dynasty", "唐朝");
        liShiMinMeta.put("role", "皇帝");
        liShiMinMeta.put("type", "历史人物");

        Map<String, Object> qinShiHuangMeta = new HashMap<>();
        qinShiHuangMeta.put("person", "嬴政（秦始皇）");
        qinShiHuangMeta.put("dynasty", "秦朝");
        qinShiHuangMeta.put("role", "开国皇帝");
        qinShiHuangMeta.put("type", "历史人物");

        Map<String, Object> zhuYuanZhangMeta = new HashMap<>();
        zhuYuanZhangMeta.put("person", "朱元璋");
        zhuYuanZhangMeta.put("dynasty", "明朝");
        zhuYuanZhangMeta.put("role", "开国皇帝");
        zhuYuanZhangMeta.put("type", "历史人物");

        var documents = List.of(
                new Document(
                        "李世民是唐朝的皇帝，他是唐朝的第二位君主，年号 “贞观”，史称 “唐太宗”。",
                        liShiMinMeta
                ),
                new Document(
                        "秦始皇（公元前 259 年 — 公元前 210 年），嬴姓，赵氏，名政，是中国历史上第一个皇帝，被誉为 “千古一帝”。",
                        qinShiHuangMeta
                ),
                new Document(
                        "朱元璋是明朝的开国皇帝，字国瑞，原名朱重八，后改名朱兴宗，濠州钟离（今安徽凤阳）人。",
                        zhuYuanZhangMeta
                )
        );

        // 3. 存储带有元数据的文档
        vectorStore.add(documents);
    }

    @GetMapping("/search")
    public List<Document> search(@RequestParam(name = "query",defaultValue = "皇帝") String query) {
        List<Document> search = vectorStore.similaritySearch(query);
        log.info("search: {}", search);
        return search;
    }

    @GetMapping("/search/top")
    public List<Document> searchTop(@RequestParam(name = "query",defaultValue = "皇帝") String query,@RequestParam(name = "topK",defaultValue = "1") int topK) {
        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .topK(topK)
                .filterExpression("role=='开国皇帝'")
                .build();
        List<Document> search = vectorStore.similaritySearch(searchRequest);
        log.info("search: {}", search);
        return search;
    }
}
