package com.dentalmoovi.website.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.dentalmoovi.website.Utils;
import com.dentalmoovi.website.models.dtos.MessageDTO;
import com.dentalmoovi.website.security.jwt.JWTprovider;
import com.dentalmoovi.website.services.UserSer;
import com.dentalmoovi.website.services.cache.CacheSer;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/public")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    private final AuthenticationManager am;
    private final JWTprovider jWTprovider;
    private final UserSer userSer;
    private final CacheSer cacheSer;
    private static Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthenticationManager am, JWTprovider jWTprovider, UserSer userSer, CacheSer cacheSer) {
        this.am = am;
        this.jWTprovider = jWTprovider;
        this.userSer = userSer;
        this.cacheSer = cacheSer;
    }

    @Value("${jwt.accessTokenByCookieName}")
    private String cookieName;

    @PostMapping("/isAuthorized")
    public ResponseEntity<Boolean> checkRole(@RequestBody LoginDTO loginUser) {

        Boolean isAdmin = userSer.isAdmin(loginUser.getUserName());

        if (Boolean.TRUE.equals(isAdmin)) {
            try {
                am.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUser.getUserName(), loginUser.getPassword())
                );

                String subject = "Codigo de inicio de sesión";
                String body =   
                "Querido Admin, Dental Moovi recibió una solicitud de inicio de sesión.\n\n"+
                "Si no realizo ningun inicio de sesión por favor comuniquelo inmediatamente al numero 314-453-6435\n\n"+
                "El codigo de confirmación es: ";

                userSer.sendEmailNotification(loginUser.getUserName(), subject, body);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        }

        return ResponseEntity.ok(isAdmin);
    }

    @PostMapping("/login")
    @CacheEvict(cacheNames = {"getUserAuthenticated", "isAdmin"})
    public ResponseEntity<Object> login( HttpServletResponse hsr,
        @Valid @RequestBody LoginDTO loginUser, BindingResult bidBindingResult){

        logger.info(loginUser.getUserName());
        if(bidBindingResult.hasErrors())
            return new ResponseEntity<>(new MessageDTO("Revise sus credenciales"), HttpStatus.BAD_REQUEST);
        try {
                if(userSer.isAdmin(loginUser.getUserName())){
                    //get verification code
                    String code = cacheSer.getFromRegistrationCache(loginUser.getUserName());

                    //verify if code sended is equals the verification code
                    if(!loginUser.getCode().equals(code)) 
                        throw new RuntimeException("That code is incorrect");

                    
                }

                Authentication auth = am.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUser.getUserName(), loginUser.getPassword())
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
                String jwt = jWTprovider.generateToken(auth);
                Utils.createCookie(hsr, cookieName, jwt, false, -1, "localhost");
                return new ResponseEntity<>(new MessageDTO("Sesión iniciada"), HttpStatus.OK);
        } catch (Exception e) {
                logger.error("Error to login: {}", e.getMessage());
                return new ResponseEntity<>(new MessageDTO("Revise sus credenciales "+e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/logout")
    @CacheEvict(cacheNames = {"getUserAuthenticated", "isAdmin"})
    public ResponseEntity<MessageDTO> logOut(HttpServletResponse hsr){
        Utils.clearCookie(hsr, cookieName);
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>(new MessageDTO("Sesión cerrada"), HttpStatus.OK);
    }

}
