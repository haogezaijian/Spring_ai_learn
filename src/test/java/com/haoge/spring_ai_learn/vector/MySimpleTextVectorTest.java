package com.haoge.spring_ai_learn.vector;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MySimpleTextVectorTest {

    @Resource
    MySimpleTextVector mySimpleTextVector;

    @Test
    void convertTextToEmbedding() {
        mySimpleTextVector.convertTextToEmbedding();
    }
}