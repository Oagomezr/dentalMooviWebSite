package com.dentalmoovi.website;

import com.dentalmoovi.website.models.cart.CartRequest;
import com.dentalmoovi.website.models.dtos.UserDTO;
import com.dentalmoovi.website.models.entities.Orders;
import com.dentalmoovi.website.models.entities.Roles;
import com.dentalmoovi.website.models.entities.Users;
import com.dentalmoovi.website.models.entities.enums.GenderList;
import com.dentalmoovi.website.models.entities.enums.StatusOrderList;
import com.dentalmoovi.website.repositories.OrdersRep;
import com.dentalmoovi.website.repositories.UserRep;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.lang.NonNull;
import org.springframework.web.util.WebUtils;

public class Utils {

    private Utils() {
        throw new IllegalStateException("Utility class");
    }

    public static void createCookie(HttpServletResponse hsr, String name, String value, 
        Boolean secure, Integer maxAge, String domain){
            Cookie cookie = new Cookie(name, value);
            cookie.setSecure(secure);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(maxAge);
            cookie.setDomain(domain);
            cookie.setPath("/");
            hsr.addCookie(cookie);
    }

    @CacheEvict(cacheNames = "getUserAuthenticated", allEntries = true)
    public static void clearCookie(HttpServletResponse hsr, String name){
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(1);
        cookie.setDomain("localhost");
        hsr.addCookie(cookie);
    }

    public static Users setUser(String firstName, String lastName, String email, String celPhone, GenderList gender,
                                String password, LocalDate birthdate, Roles role , UserRep repository){
        Users user = new Users();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setCelPhone(celPhone);
        user.setGender(gender);
        user.setPassword(password);
        user.setBirthdate(birthdate);
        user.addRole(role);
        user = repository.save(user);
        return user;
    }

    public static byte[] loadImageData(String imagePath) {
        try {
            Path path = Paths.get(imagePath);
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException("Error to load the image: " + imagePath, e);
        }
    }

    public static UserDTO setUserDTO(String name, String lastName, String email, String celPhone, GenderList gender, 
                    LocalDate birthdate, String code, String password){
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(name);
        userDTO.setLastName(lastName);
        userDTO.setEmail(email);
        userDTO.setCelPhone(celPhone);
        userDTO.setGender(gender);
        userDTO.setBirthdate(birthdate);
        userDTO.setCode(code);
        userDTO.setPassword(password);
        return userDTO;
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static String transformToJSON(Object object) {
        try {
            return objectMapper.writeValueAsString(object)
                    .replaceAll("[áÁäÄâÂàÀãÃ]", "Ã")
                    .replaceAll("[éÉëËêÊèÈẽẼ]", "Ã")
                    .replaceAll("[íÍïÏîÎìÌĩĨ]", "Ã")
                    .replaceAll("[óÓöÖôÔòÒõÕ]", "Ã")
                    .replaceAll("[úÚüÜûÛùÙũŨ]", "Ã");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al convertir a JSON: " + e.getMessage());
        }
    }

    public static Orders setOrder(StatusOrderList status, long idUser, long idAddress, CartRequest req, OrdersRep rep){
        Orders order = new Orders(null, null, status, idUser, idAddress, null);
        req.getData().forEach(elem ->
            order.addProduct(elem.getId(), elem.getAmount()));
        return rep.save(order);
    }

    public static String getToken(@NonNull HttpServletRequest request, @NonNull String  cookieName){
        Cookie cookie = WebUtils.getCookie( request, cookieName);
        return cookie != null ? cookie.getValue() : null;
    }
}
