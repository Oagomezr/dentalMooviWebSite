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

import com.dentalmoovi.website.Utils;
import com.dentalmoovi.website.models.dtos.UserDTO;
import com.dentalmoovi.website.models.entities.Roles;
import com.dentalmoovi.website.models.enums.GenderList;
import com.dentalmoovi.website.models.enums.RolesList;
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
        Utils.setUser("example", "test", "example@test.com", "333-3333-333", GenderList.UNDEFINED, "12345", null, defaultRole, userRep);


        UserDTO userDTO1 = Utils.setUserDTO("example", "test", "example@test.com", "333-3333-3333", GenderList.UNDEFINED, null, "123456", "password");
        UserDTO userDTO2 = Utils.setUserDTO("example2", "test2", "example2@test.com", "222-2222-2222", GenderList.UNDEFINED, null, "654321", "password");
        UserDTO userDTO3 = Utils.setUserDTO("example3", "test3", "example3@test.com", "111-1111-1111", GenderList.UNDEFINED, null, "123456", "password");

        cacheSer.addToOrUpdateRegistrationCache("example2@test.com", "123456");
        cacheSer.addToOrUpdateRegistrationCache("example3@test.com", "123456");

        assertThrows(RuntimeException.class, () -> userSer.createUser(userDTO1));
        assertThrows(RuntimeException.class, () -> userSer.createUser(userDTO2));
        userDTO2.setCode("123456");
        assertDoesNotThrow(() -> userSer.createUser(userDTO2));

        assertEquals("User Created", userSer.createUser(userDTO3));
    }
}