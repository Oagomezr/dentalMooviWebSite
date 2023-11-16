package com.dentalmoovi.website.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.dentalmoovi.website.Utils;
import com.dentalmoovi.website.models.dtos.ProductsDTO;
import com.dentalmoovi.website.models.responses.ProductsResponse;
import com.dentalmoovi.website.services.ProductsSer;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.NONE)
class ProductsControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ProductsSer productsSer;

    private ProductsResponse expectedResponse;

    @Test
    void getProductsByCategoryTest() throws Exception{
        
        expectedResponse = productsSer.getProductsByCategory("ORTODONCIA", 1, 9);

        String expectedJsonResponse = Utils.transformToJSON(expectedResponse);

        mockMvc.perform(get("/public/products/category/ORTODONCIA/1/9")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedJsonResponse));
    }

    @Test
    void getProductsByContainingTest() throws Exception{
        
        expectedResponse = productsSer.getProductsByContaining("ar", true, 1, 9);
        ProductsResponse expectedResponse2 = productsSer.getProductsByContaining("ar", false, 1, 9);

        String expectedJsonResponse1 = Utils.transformToJSON(expectedResponse);
        String expectedJsonResponse2 = Utils.transformToJSON(expectedResponse2);

        mockMvc.perform(get("/public/products/search/ar/true/1/9")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedJsonResponse1));

        mockMvc.perform(get("/public/products/search/ar/false/1/9")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedJsonResponse2));
    }

    @Test
    void getProductTest() throws Exception{
        
        ProductsDTO expectedResponse = productsSer.getProduct("pinzaOrtodoncia1");

        String expectedJsonResponse = Utils.transformToJSON(expectedResponse);

        mockMvc.perform(get("/public/products/pinzaOrtodoncia1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedJsonResponse));
    }
}
