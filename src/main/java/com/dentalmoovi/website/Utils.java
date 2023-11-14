package com.dentalmoovi.website;

import com.dentalmoovi.website.models.dtos.UserDTO;
import com.dentalmoovi.website.models.entities.Categories;
import com.dentalmoovi.website.models.entities.Images;
import com.dentalmoovi.website.models.entities.Products;
import com.dentalmoovi.website.models.entities.Roles;
import com.dentalmoovi.website.models.entities.Users;
import com.dentalmoovi.website.models.enums.GenderList;
import com.dentalmoovi.website.models.enums.RolesList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public class Utils {

    private Utils() {
        throw new IllegalStateException("Utility class");
    }

    public static Categories setCategory(String name, Categories parentCategory){
        Categories category = new Categories();
        category.setName(name);
        category.setParentCategory(parentCategory);
        category.setNumberUpdates(1);
        return category;
    }

    public static Products setProduct(String name, String description, double unitPrice, int stock, 
                                        Categories category, boolean openToPublic){
        Products product = new Products();
        product.setName(name);
        product.setDescription(description);
        product.setUnitPrice(unitPrice);
        product.setStock(stock);
        product.setCategory(category);
        product.setOpenToPublic(openToPublic);
        return product;
    }

    public static Users setUser(String firstName, String lastName, String email, String celPhone, GenderList gender, String password, LocalDate birthdate){
        Users user = new Users();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setCelPhone(celPhone);
        user.setGender(gender);
        user.setPassword(password);
        user.setBirthdate(birthdate);
        return user;
    }

    public static Roles setRole(RolesList roleType){
        Roles role = new Roles();
        role.setRole(roleType);
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

    public static UserDTO setUserDTO(String name, String lastName, String email, String celPhone, GenderList gender, LocalDate birthdate){
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(name);
        userDTO.setLastName(lastName);
        userDTO.setEmail(email);
        userDTO.setCelPhone(celPhone);
        userDTO.setGender(gender);
        userDTO.setBirthdate(birthdate);
        return userDTO;
    }

    public static Images setImage(String name, String contentType, byte[] data, Products product){
        Images img = new Images();
        img.setName(name);
        img.setContentType(contentType);
        img.setData(data);
        img.setProduct(product);
        return img;
    }
}
