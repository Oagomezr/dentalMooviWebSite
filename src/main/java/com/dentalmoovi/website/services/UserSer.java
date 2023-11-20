package com.dentalmoovi.website.services;

import java.util.Random;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dentalmoovi.website.Utils;
import com.dentalmoovi.website.models.dtos.UserDTO;
import com.dentalmoovi.website.models.entities.Roles;
import com.dentalmoovi.website.models.enums.RolesList;
import com.dentalmoovi.website.repositories.RolesRep;
import com.dentalmoovi.website.repositories.UserRep;
import com.dentalmoovi.website.services.cache.CacheSer;

@Service
public class UserSer {

    Random random = new Random();
    private final CacheSer cacheSer;
    private final EmailSer emailSer;
    private final UserRep userRep;
    private final RolesRep rolesRep;

    
    public UserSer(CacheSer cacheSer, EmailSer emailSer, UserRep userRep, RolesRep rolesRep) {
        this.cacheSer = cacheSer;
        this.emailSer = emailSer;
        this.userRep = userRep;
        this.rolesRep = rolesRep;
    }

    public void sendEmailNotification(String email) {
        String retrictReplay = cacheSer.getFromReplayCodeRestrict(email);
        if (retrictReplay !=null && retrictReplay.equals(email)) return;

        //Generate randomNumber
        int randomNumber = random.nextInt(1000000);

        //generate a format to add zeros to the left in case randomNumber < 100000
        String formattedNumber = String.format("%06d", randomNumber);

        String subject = "Codigo de confirmación";
        String body = "Dental Moovi recibió una solicitud de registro.\n\n"+
                        "El codigo de confirmación es: "+ formattedNumber;
        
        cacheSer.addToOrUpdateRegistrationCache(email, formattedNumber);
        cacheSer.addToOrUpdateReplayCodeRestrict(email, email);

        try {
            emailSer.sendEmail(email, subject, body);
        } catch (Exception e) {
            // Manage the exception in case it cannot send the email
            e.printStackTrace();
        }
    }

    public String createUser(UserDTO userDTO) throws RuntimeException {

        class CreateUser{

            /* ¡¡PLEASE PAY CLOSE ATTENTION ONLY THIS "createUser()" METHOD TO UNDERSTAND THIS SERVICE!! */ 
            String createUser() throws RuntimeException{
                //verify if email exist
                if(checkEmailExists(userDTO.getEmail())) 
                    throw new RuntimeException("That user already exist");

                //get verification code
                String code = cacheSer.getFromRegistrationCache(userDTO.getEmail());

                //verify if code sended is equals the verification code
                if(!userDTO.getCode().equals(code)) 
                    throw new RuntimeException("That code is incorrect");

                //create default role
                Roles defaultRole = rolesRep.findByRole(RolesList.USER_ROLE)
                    .orElseThrow(() -> new RuntimeException("Role not found"));

                //encrypt the password
                String hashedPassword = new BCryptPasswordEncoder().encode(userDTO.getPassword()); 

                //set and save user
                Utils.setUser(userDTO.getFirstName(), userDTO.getLastName(), 
                    userDTO.getEmail(), userDTO.getCelPhone(), userDTO.getGender(), hashedPassword, userDTO.getBirthdate(), defaultRole, userRep);

                return "User Created";
            }
        }

        CreateUser innerClass = new CreateUser();
        return innerClass.createUser();
    }

    //verify if user exist
    public boolean checkEmailExists(String value) {
        return userRep.existsByEmail(value);
    }

}
