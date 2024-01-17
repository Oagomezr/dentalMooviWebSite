package com.dentalmoovi.website.controllers;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dentalmoovi.website.models.cart.CartRequest;
import com.dentalmoovi.website.services.OrdersSer;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class OrdersController {
    private final OrdersSer ordersSer;

    @PostMapping("/user/generateOrder/{idAddress}")
    public ResponseEntity<Boolean> createProduct(@RequestBody CartRequest req, @PathVariable long idAddress) {
        try{
            ordersSer.generateOrder(req, idAddress);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/public/generate-pdf")
    public String generatePdf(Model model) {
        try {
            List<String> items = Arrays.asList("Item 1", "Item 2", "Item 3");
            model.addAttribute("items", items);
            // Lógica para generar el PDF (si es necesario)
            return "pdfOrder";
        } catch (Exception e) {
            // Imprimir mensaje de error en la consola de Spring Boot
            System.err.println("Error al generar el PDF: " + e.getMessage());
            return "errorPage"; // Puedes redirigir a una página de error si lo deseas
        }
    }
}
