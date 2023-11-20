package com.dentalmoovi.website.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.dentalmoovi.website.models.dtos.UserDTO;
import com.dentalmoovi.website.services.UserSer;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserSer userSer;

    public UserController(UserSer userSer) {
        this.userSer = userSer;
    }

    @PostMapping("/public/create")
    public ResponseEntity<Void> createUser(@RequestBody UserDTO userDTO){
        try {
            userSer.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/public/sendEmail")
    public void sendMessage(@RequestBody String email){
        userSer.sendEmailNotification(email);
    }

    @GetMapping("/public/{email}")
    public boolean checkEmailExists(@PathVariable String email) {
        return userSer.checkEmailExists(email);
    }
}
