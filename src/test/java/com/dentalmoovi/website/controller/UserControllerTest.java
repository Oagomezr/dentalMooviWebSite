package com.dentalmoovi.website.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import com.dentalmoovi.website.models.dtos.UserDTO;
import com.dentalmoovi.website.models.enums.GenderList;
import com.dentalmoovi.website.services.cache.CacheSer;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.NONE)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CacheSer cacheSer;

    @SuppressWarnings("null")
    @Test
    void createUserTest() throws Exception{
        UserDTO userDTO = Utils.setUserDTO("example", "test", "example@test.com", "333-3333-3333", GenderList.UNDEFINED, null, "123456", "password");

        String userJson = objectMapper.writeValueAsString(userDTO);

        cacheSer.addToOrUpdateRegistrationCache("example@test.com", "123456");

        mockMvc.perform(post("/public/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/public/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isBadRequest());
    }

}
