package com.dentalmoovi.website.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dentalmoovi.website.models.responses.CategoriesResponse;
import com.dentalmoovi.website.services.CategoriesSer;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:4200")
public class CategoriesController {
    private final CategoriesSer categoriesSer;

    public CategoriesController(CategoriesSer categoriesSer) {
        this.categoriesSer = categoriesSer;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<CategoriesResponse> getAllCategories() {
        try{
            return ResponseEntity.ok(categoriesSer.getAllCategories());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
