package com.dentalmoovi.website.integration.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import com.dentalmoovi.website.Utils;
import com.dentalmoovi.website.models.entities.Roles;
import com.dentalmoovi.website.models.entities.Users;
import com.dentalmoovi.website.models.enums.GenderList;
import com.dentalmoovi.website.models.enums.RolesList;
import com.dentalmoovi.website.repositories.RolesRep;
import com.dentalmoovi.website.repositories.UserRep;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@SpringBootTest
class UserRepAndRolesRepTest {
    
    @Autowired
    private UserRep usersRep;

    @Autowired
    private RolesRep rolesRep;

    //set roles
    private static Roles userRole;
    private static Roles adminRole;

    //set users
    private static Users user1;
    private static Users user2;

    @BeforeAll
    static void setUp(@Autowired UserRep usersRep, @Autowired RolesRep rolesRep){
        userRole = Utils.setRole(RolesList.USER_ROLE, rolesRep);
        adminRole = Utils.setRole(RolesList.ADMIN_ROLE, rolesRep);

        user1 = Utils.setUser("one", "number", "one@mail.com", "0123456789", GenderList.FEMALE, "12345", null, userRole, usersRep);
        user2 = Utils.setUser("two", "number", "two@mail.com", "9876543210", GenderList.MALE, "12345", null, userRole, usersRep);
    }

    @Test
    void findByNameRoleTest() throws Exception{
        Roles defaultRole = rolesRep.findByRole(RolesList.USER_ROLE).
                                orElseThrow(() -> new Exception("Role not found"));
        Roles highRole = rolesRep.findByRole(RolesList.ADMIN_ROLE).
                                orElseThrow(() -> new Exception("Role not found"));
        
        assertEquals(defaultRole, userRole);
        assertEquals(highRole, adminRole);
    }

    @Test
    void findByEmailTest() throws Exception{

        Users userOne = usersRep.findByEmail(user1.getEmail()).
                        orElseThrow(() -> new Exception("User not found"));
        Users userTwo = usersRep.findByEmail(user2.getEmail()).
                        orElseThrow(() -> new Exception("User not found"));

        assertEquals(userOne, user1);
        assertEquals(userTwo, user2);
    }

    @Test
    void existsByEmail(){
        boolean trueCase = usersRep.existsByEmail(user1.getEmail());
        boolean falseCase = usersRep.existsByEmail("noExist@neverExist.no.exist");
        assertTrue(trueCase);
        assertFalse(falseCase);
    }
}
