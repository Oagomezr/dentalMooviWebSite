package com.dentalmoovi.website;

import com.dentalmoovi.website.models.cart.CartRequest;
import com.dentalmoovi.website.models.dtos.AddressesDTO;
import com.dentalmoovi.website.models.dtos.UserDTO;
import com.dentalmoovi.website.models.entities.Addresses;
import com.dentalmoovi.website.models.entities.Categories;
import com.dentalmoovi.website.models.entities.Images;
import com.dentalmoovi.website.models.entities.Orders;
import com.dentalmoovi.website.models.entities.Products;
import com.dentalmoovi.website.models.entities.Roles;
import com.dentalmoovi.website.models.entities.Users;
import com.dentalmoovi.website.models.enums.GenderList;
import com.dentalmoovi.website.models.enums.RolesList;
import com.dentalmoovi.website.models.enums.StatusOrderList;
import com.dentalmoovi.website.repositories.AddressesRep;
import com.dentalmoovi.website.repositories.CategoriesRep;
import com.dentalmoovi.website.repositories.ImgRep;
import com.dentalmoovi.website.repositories.OrdersRep;
import com.dentalmoovi.website.repositories.ProductsRep;
import com.dentalmoovi.website.repositories.RolesRep;
import com.dentalmoovi.website.repositories.UserRep;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import org.springframework.cache.annotation.CacheEvict;

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

    public static Categories setCategory(String name, Long idParentCategory, CategoriesRep repository){
        Categories category = new Categories();
        category.setName(name);
        category.setIdParentCategory(idParentCategory);
        category = repository.save(category);
        return category;
    }

    public static Products setProduct(String name, String description, String shortDescription, double unitPrice, int stock, 
                                        Long idCategory, boolean openToPublic, ProductsRep repository){
        Products product = new Products();
        product.setName(name);
        product.setDescription(description);
        product.setShortDescription(shortDescription);
        product.setUnitPrice(unitPrice);
        product.setStock(stock);
        product.setIdCategory(idCategory);
        product.setOpenToPublic(openToPublic);
        product = repository.save(product);
        return product;
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

    public static Roles setRole(RolesList roleType, RolesRep repository){
        Roles role = new Roles();
        role.setRole(roleType);
        role = repository.save(role);
        return role;
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

    public static Images setImage(String name, String contentType, byte[] data, Long idProduct, ImgRep repository){
        Images img = new Images();
        img.setName(name);
        img.setContentType(contentType);
        img.setData(data);
        img.setIdProduct(idProduct);
        img = repository.save(img);
        return img;
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

    public static Addresses setAddress(String departament, String location, String addressLocation, String phone, String description, AddressesRep rep){
        Addresses address = new Addresses();
        address.setDepartament(departament);
        address.setLocation(location);
        address.setAddress(addressLocation);
        address.setPhone(phone);
        address.setDescription(description);
        address = rep.save(address);
        return address;
    }

    public static AddressesDTO setAddressDTO(long id, String departament, String location, String address, String phone, String description){
        AddressesDTO addressDTO = new AddressesDTO();
        addressDTO.setId(id);
        addressDTO.setDepartament(departament);
        addressDTO.setLocation(location);
        addressDTO.setAddress(address);
        addressDTO.setPhone(phone);
        addressDTO.setDescription(description);
        return addressDTO;
    }

    public static Orders setOrder(StatusOrderList status, String file, long idUser, long idAddress, CartRequest req, OrdersRep rep){
        Orders order = new Orders();
        order.setStatus(status);
        order.setOrderFile(file);
        order.setIdUser(idUser);
        order.setIdAddress(idAddress);
        req.getData().forEach(elem ->
            order.addProduct(elem.getId(), elem.getAmount()));
        return rep.save(order);
    }
}
