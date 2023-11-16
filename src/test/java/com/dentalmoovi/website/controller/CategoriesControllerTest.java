package com.dentalmoovi.website.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.dentalmoovi.website.Utils;
import com.dentalmoovi.website.models.responses.CategoriesResponse;
import com.dentalmoovi.website.services.CategoriesSer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.NONE)
class CategoriesControllerTest {
    
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private CategoriesSer categoriesSer;

    @Test
    void getAllCategories() throws Exception{

        CategoriesResponse expectedResponse = categoriesSer.getAllCategories();

        String expectedJsonResponse = Utils.transformToJSON(expectedResponse);

        mockMvc.perform(get("/public/categories")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedJsonResponse));
    }
}
