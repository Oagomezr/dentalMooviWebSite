package com.dentalmoovi.website.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.dentalmoovi.website.models.dtos.AddressesDTO;
import com.dentalmoovi.website.models.dtos.MessageDTO;
import com.dentalmoovi.website.models.dtos.UserDTO;
import com.dentalmoovi.website.models.responses.AddressesResponse;
import com.dentalmoovi.website.services.UserSer;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class UserController {
    private final UserSer userSer;

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
        String subject = "Codigo de confirmación";
        String body = "Dental Moovi recibió una solicitud de registro.\n\n"+
                        "El codigo de confirmación es: ";
        userSer.sendEmailNotification(email, subject, body);
    }

    @GetMapping("/public/{email}")
    public boolean checkEmailExists(@PathVariable String email) {
        return userSer.checkEmailExists(email);
    }

    
    @GetMapping("/user/getUser")
    public ResponseEntity<UserDTO> getUserAuthenticated(){
        try {
            UserDTO userDTO= userSer.getUserAuthenticated();
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    
    @PutMapping("/user/update")
    public ResponseEntity<MessageDTO> updateUser(@RequestBody UserDTO userDTO){
        try {
            return ResponseEntity.ok(userSer.updateUserInfo(userDTO));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/user/addAddress")
    public ResponseEntity<MessageDTO> createAddress(@RequestBody AddressesDTO addressDTO){
        try {
            userSer.createAddress(addressDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/user/getAddresses")
    public ResponseEntity<AddressesResponse> getAddresses(){
        try {
            return ResponseEntity.ok(userSer.getAddresses());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/user/updateAddress")
    public ResponseEntity<MessageDTO> updateAddress(@RequestBody AddressesDTO addressDTO){
        try {
            return ResponseEntity.ok(userSer.updateAddress(addressDTO));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/user/deleteAddress/{id}")
    public ResponseEntity<MessageDTO> deleteAddress(@PathVariable long id){
        try{
            return ResponseEntity.ok(userSer.deleteAddress(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
