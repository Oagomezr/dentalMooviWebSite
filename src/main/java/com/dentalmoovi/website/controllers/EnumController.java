package com.dentalmoovi.website.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dentalmoovi.website.services.EnumSer;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:4200")
public class EnumController {

    private final EnumSer enumSer;

    @GetMapping("/user/departaments/{name}")
    public ResponseEntity<Object> getDepartamentsByContaining(@PathVariable String name){
        try{
            return ResponseEntity.ok(enumSer.getDepartamentsByContaining(name));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    } 

    @GetMapping("/user/municipalies/{name}/{id}")
    public ResponseEntity<Object> getLineasVehiculo(@PathVariable String name, @PathVariable int id) {
        try{
            return ResponseEntity.ok(enumSer.getMunicipalyByContaining(name, id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/admin/categories/{name}")
    public ResponseEntity<Object> getCategoriesByAdmin(@PathVariable String name) {
        try{
            return ResponseEntity.ok(enumSer.getCategoriesAdmin(name));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    public EnumController(EnumSer enumSer) {
        this.enumSer = enumSer;
    }
}
