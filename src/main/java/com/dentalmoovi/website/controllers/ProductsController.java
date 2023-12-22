package com.dentalmoovi.website.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dentalmoovi.website.models.dtos.MessageDTO;
import com.dentalmoovi.website.models.dtos.ProductsDTO;
import com.dentalmoovi.website.models.responses.ProductsResponse;
import com.dentalmoovi.website.services.ProductsSer;

import io.jsonwebtoken.io.IOException;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:4200")
public class ProductsController {
    private final ProductsSer productsSer;

    public ProductsController(ProductsSer productsSer) {
        this.productsSer = productsSer;
    }

    private static Logger logger = LoggerFactory.getLogger(ProductsController.class);

    @GetMapping("/public/products/search/{name}/{limit}/{pageNumber}/{pageSize}")
    public ResponseEntity<ProductsResponse> getProductsByContaining(@PathVariable String name, @PathVariable boolean limit, @PathVariable int pageNumber, @PathVariable int pageSize){
        try {
            ProductsResponse products = productsSer.getProductsByContaining(name, limit, pageNumber, pageSize, false);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/public/products/{name}")
    public ResponseEntity<ProductsDTO> getProduct(@PathVariable String name){
        try {
            ProductsDTO products = productsSer.getProduct(name, false);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/public/products/category/{name}/{pageNumber}/{pageSize}")
    public ResponseEntity<ProductsResponse> getProductsByCategory(
        @PathVariable String name, @PathVariable int pageNumber, @PathVariable int pageSize){
        try {
            ProductsResponse products = productsSer.getProductsByCategory(name, pageNumber, pageSize, false);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/admin/products/updateMainImage/{productName}")
    public ResponseEntity<MessageDTO> updateCategoryLocation(@PathVariable String productName, @RequestBody long idImage) {
        try{
            return ResponseEntity.ok(productsSer.updateMainImage(idImage, productName));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/admin/products/uploadImage/{productName}")
    public ResponseEntity<MessageDTO> handleFileUpload(@RequestPart("file") MultipartFile file, @PathVariable String productName) throws java.io.IOException {
        if (file.isEmpty()) {
            logger.info("Archivo vacío");
            return ResponseEntity.badRequest().body(new MessageDTO("Archivo vacío"));
        }
        try {
            return ResponseEntity.ok(productsSer.uploadImage(file, productName));
        } catch (IOException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(500).body(new MessageDTO("Error al procesar el archivo: " + e.getMessage()));
        }
    }

    @DeleteMapping("/admin/products/deleteImage/{parameter}")
    public ResponseEntity<MessageDTO> deleteImage(@PathVariable String parameter) {
        try{
            return ResponseEntity.ok(productsSer.deleteImage(parameter));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/admin/products/visibility/{productName}")
    public ResponseEntity<MessageDTO> changeVisibilityProduct(@PathVariable String productName, @RequestBody boolean visibility) {
        try{
            return ResponseEntity.ok(productsSer.hideOrShowProduct(visibility, productName));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/admin/products/updateProductInfo/{productName}/{option}")
    public ResponseEntity<MessageDTO> updateProductInfo(@PathVariable String productName, @PathVariable int option, @RequestBody String newInfo) {
        try{
            return ResponseEntity.ok(productsSer.updateProductInfo(option, productName, newInfo));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/admin/products/createProduct")
    public ResponseEntity<Boolean> createProduct(@RequestBody String categoryName) {
        try{
            return ResponseEntity.ok(productsSer.createProduct(categoryName));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/admin/products/search/{name}/{limit}/{pageNumber}/{pageSize}")
    public ResponseEntity<ProductsResponse> getProductsByContainingA(@PathVariable String name, @PathVariable boolean limit, @PathVariable int pageNumber, @PathVariable int pageSize){
        try {
            ProductsResponse products = productsSer.getProductsByContaining(name, limit, pageNumber, pageSize, true);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/admin/products/{name}")
    public ResponseEntity<ProductsDTO> getProductA(@PathVariable String name){
        try {
            ProductsDTO products = productsSer.getProduct(name, true);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/admin/products/category/{name}/{pageNumber}/{pageSize}")
    public ResponseEntity<ProductsResponse> getProductsByCategoryA(
        @PathVariable String name, @PathVariable int pageNumber, @PathVariable int pageSize){
        try {
            ProductsResponse products = productsSer.getProductsByCategory(name, pageNumber, pageSize, true);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
