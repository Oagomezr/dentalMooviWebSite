package com.dentalmoovi.website.integration.services;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.dentalmoovi.website.models.dtos.UserDTO;
import com.dentalmoovi.website.models.entities.Roles;
import com.dentalmoovi.website.models.entities.Users;
import com.dentalmoovi.website.models.entities.enums.GenderList;
import com.dentalmoovi.website.models.entities.enums.RolesList;
import com.dentalmoovi.website.repositories.RolesRep;
import com.dentalmoovi.website.repositories.UserRep;
import com.dentalmoovi.website.services.UserSer;
import com.dentalmoovi.website.services.cache.CacheSer;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
class UserSerIntegrationTest {

    @Autowired
    private UserSer userSer;

    @Autowired
    private UserRep userRep;

    @Autowired
    private CacheSer cacheSer;

    @Autowired
    private RolesRep rolesRep;

    @Test
    void createUserTest(){

        
        Roles defaultRole = rolesRep.findByRole(RolesList.USER_ROLE)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
        Users user = new Users(null, "example", "test", "example@test.com", "333-3333-333", null, GenderList.UNDEFINED, "12345", null, null);
        user.addRole(defaultRole);
        userRep.save(user);


        UserDTO userDTO1 = new UserDTO(null, "example", "test", "example@test.com", "333-3333-3333", null, GenderList.UNDEFINED, "123456", "password");
        UserDTO userDTO2 = new UserDTO(null, "example2", "test2", "example2@test.com", "222-2222-2222", null, GenderList.UNDEFINED, "654321", "password");
        UserDTO userDTO3 = new UserDTO(null, "example3", "test3", "example3@test.com", "111-1111-1111", null, GenderList.UNDEFINED, "123456", "password");

        cacheSer.addToOrUpdateRegistrationCache("example2@test.com", "123456");
        cacheSer.addToOrUpdateRegistrationCache("example3@test.com", "123456");

        assertThrows(RuntimeException.class, () -> userSer.createUser(userDTO1));
        assertThrows(RuntimeException.class, () -> userSer.createUser(userDTO2));
        UserDTO userDTO4 = new UserDTO(null, "example2", "test2", "example2@test.com", "222-2222-2222", null, GenderList.UNDEFINED, "123456", "password");
        assertDoesNotThrow(() -> userSer.createUser(userDTO4));

        assertEquals("User Created", userSer.createUser(userDTO3));
    }
}