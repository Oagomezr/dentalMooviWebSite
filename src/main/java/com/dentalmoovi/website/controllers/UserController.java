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
import com.dentalmoovi.website.security.LoginDTO;
import com.dentalmoovi.website.security.PwDTO;
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PostMapping("/public/sendEmail")
    public void sendMessage(@RequestBody String email){
        String subject = "Codigo de confirmación";
        String body = "Dental Moovi recibió una solicitud.\n\n"+
                        "El codigo de confirmación es: ";
        userSer.sendEmailNotification(email, subject, body);
    }

    @GetMapping("/public/{email}/{signup}")
    public boolean checkEmailExists(@PathVariable String email, @PathVariable boolean signup) {
        return userSer.checkEmailExists(email, signup);
    }
    
    @GetMapping("/user/getUser/{cacheRef}")
    public ResponseEntity<UserDTO> getUserAuthenticated(@PathVariable String cacheRef){
        try {
            UserDTO userDTO = userSer.getUserAuthDTO(cacheRef);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    
    @PutMapping("/user/update/{cacheRef}")
    public ResponseEntity<MessageDTO> updateUser(@RequestBody UserDTO userDTO, @PathVariable String cacheRef){
        try {
            return ResponseEntity.ok(userSer.updateUserInfo(userDTO, cacheRef));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/user/addAddress/{cacheRef}")
    public ResponseEntity<MessageDTO> createAddress(@RequestBody AddressesDTO addressDTO, @PathVariable String cacheRef){
        try {
            userSer.createAddress(addressDTO, cacheRef);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/user/getAddresses/{cacheRef}")
    public ResponseEntity<AddressesResponse> getAddresses(@PathVariable String cacheRef){
        try {
            return ResponseEntity.ok(userSer.getAddresses(cacheRef));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/user/updateAddress/{cacheRef}")
    public ResponseEntity<MessageDTO> updateAddress(@RequestBody AddressesDTO addressDTO, @PathVariable String cacheRef){
        try {
            return ResponseEntity.ok(userSer.updateAddress(addressDTO, cacheRef));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/user/deleteAddress/{id}/{cacheRef}")
    public ResponseEntity<MessageDTO> deleteAddress(@PathVariable long id, @PathVariable String cacheRef){
        try{
            return ResponseEntity.ok(userSer.deleteAddress(id, cacheRef));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/name/{cacheRef}")
    public ResponseEntity<MessageDTO> getName(@PathVariable String cacheRef){
        return ResponseEntity.ok(userSer.getName(cacheRef));
    }

    @PutMapping("/user/upw/{cacheRef}")
    public ResponseEntity<MessageDTO> updatePw(
        @RequestBody PwDTO pwDto, @PathVariable String cacheRef){
        try {
            return ResponseEntity.ok(userSer.changePw(pwDto, cacheRef));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/public/rpw")
    public ResponseEntity<MessageDTO> rememberPw(
        @RequestBody LoginDTO userCredentials){
        try {
            return ResponseEntity.ok(userSer.rememberPw(userCredentials));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
