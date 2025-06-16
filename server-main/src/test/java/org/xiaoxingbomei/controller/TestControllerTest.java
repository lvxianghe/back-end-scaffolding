package org.xiaoxingbomei.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller 测试示例
 * 学习Spring Boot Web层测试
 */
@WebMvcTest(TestController.class)
public class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHelloEndpoint() throws Exception {
        mockMvc.perform(get("/test/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World!"));
    }

    @Test
    public void testJsonResponse() throws Exception {
        mockMvc.perform(get("/test/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello JSON"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
} 