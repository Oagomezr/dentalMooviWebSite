package com.dentalmoovi.website.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dentalmoovi.website.models.dtos.ProductsDTO;
import com.dentalmoovi.website.models.responses.ProductsResponse;
import com.dentalmoovi.website.services.ProductsSer;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:4200")
public class ProductsController {
    private final ProductsSer productsSer;

    public ProductsController(ProductsSer productsSer) {
        this.productsSer = productsSer;
    }

    @GetMapping("/public/products/search/{name}/{limit}/{pageNumber}/{pageSize}")
    public ResponseEntity<ProductsResponse> getProductsByContaining(@PathVariable String name, @PathVariable boolean limit, @PathVariable int pageNumber, @PathVariable int pageSize){
        try {
            ProductsResponse products = productsSer.getProductsByContaining(name, limit, pageNumber, pageSize);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/public/products/{name}")
    public ResponseEntity<ProductsDTO> getProduct(@PathVariable String name){
        try {
            ProductsDTO products = productsSer.getProduct(name);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/public/products/category/{name}/{pageNumber}/{pageSize}")
    public ResponseEntity<ProductsResponse> getProductsByCategory(
        @PathVariable String name, @PathVariable int pageNumber, @PathVariable int pageSize){
        try {
            ProductsResponse products = productsSer.getProductsByCategory(name, pageNumber, pageSize);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
