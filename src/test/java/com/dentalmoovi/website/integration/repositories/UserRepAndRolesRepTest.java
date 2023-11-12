package com.dentalmoovi.website.integration.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;

import com.dentalmoovi.website.Utils;
import com.dentalmoovi.website.models.entities.Roles;
import com.dentalmoovi.website.models.entities.Users;
import com.dentalmoovi.website.models.enums.GenderList;
import com.dentalmoovi.website.models.enums.RolesList;
import com.dentalmoovi.website.repositories.RolesRep;
import com.dentalmoovi.website.repositories.UserRep;

import jakarta.annotation.PostConstruct;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class UserRepAndRolesRepTest {
    
    @Autowired
    private UserRep usersRep;

    @Autowired
    private RolesRep rolesRep;

    //set roles
    private static Roles userRole = Utils.setRole(RolesList.USER_ROLE);
    private static Roles adminRole = Utils.setRole(RolesList.ADMIN_ROLE);

    //define the USER role
    private static Set<Roles> defaultRole = new HashSet<>(List.of(userRole));

    //set users
    private static Users user1 = Utils.setUser("one", "number", "one@mail.com", "0123456789", GenderList.FEMALE, "12345", null);
    private static Users user2 = Utils.setUser("two", "number", "two@mail.com", "9876543210", GenderList.MALE, "12345", null);

    @TestConfiguration
    static class TestConfig {

        @Autowired
        private UserRep usersRep;

        @Autowired
        private RolesRep rolesRep;

        //Execute before all test
        @PostConstruct
        void init() {
            //set
            user1.setRoles(defaultRole);
            user2.setRoles(defaultRole);
            
            //create roles to database
            rolesRep.save(userRole);
            rolesRep.save(adminRole);

            //create users to database
            usersRep.save(user1);
            usersRep.save(user2);

            //put in database immediately
            usersRep.flush();
        }
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
