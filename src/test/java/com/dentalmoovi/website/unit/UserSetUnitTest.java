package com.dentalmoovi.website.unit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dentalmoovi.website.models.dtos.UserDTO;
import com.dentalmoovi.website.models.entities.Roles;
import com.dentalmoovi.website.models.entities.enums.GenderList;
import com.dentalmoovi.website.models.entities.enums.RolesList;
import com.dentalmoovi.website.repositories.RolesRep;
import com.dentalmoovi.website.repositories.UserRep;
import com.dentalmoovi.website.services.EmailSer;
import com.dentalmoovi.website.services.UserSer;
import com.dentalmoovi.website.services.cache.CacheSer;

@ExtendWith(MockitoExtension.class)
class UserSetUnitTest {

    @Mock
    private CacheSer cacheSer;

    @Mock
    private EmailSer emailSer;

    @Mock
    private UserRep userRep;

    @Mock
    private RolesRep rolesRep;

    @InjectMocks
    private UserSer userSer;

    @BeforeEach
    void beforeEach(){
        lenient().when(userRep.existsByEmail(Mockito.any())).thenAnswer(invocation -> {
            String email = invocation.getArgument(0);
            switch (email) {
                case "test@exist.com":
                    return true;
                default:
                    return false;
            }
        });
    }
    
    @Test
    void testSendEmailNotification() {
        //call the sendEmailNotification with fake email
        userSer.sendEmailNotification("test@example.com", "test@example.com", "test@example.com");

        //verify addToOrUpdateRegistrationCache had been called with correct parameters
        verify(cacheSer, times(1)).addToOrUpdateRegistrationCache(eq("test@example.com"), anyString());

        //verify sendEmail had been called with correct parameters
        verify(emailSer, times(1)).sendEmail(eq("test@example.com"), eq("Codigo de confirmación"), anyString());
    }

    @Test
    void checkEmailExistsTest(){
        assertTrue(userSer.checkEmailExists("test@exist.com"));
        assertFalse(userSer.checkEmailExists("test@noExist.com"));
    }

    @SuppressWarnings("null")
    @Test
    void createUserTest(){

        UserDTO userDTO1 = new UserDTO(null, "userDTO1", "DTO", "test@exist.co", "333-3333-3333", null, GenderList.UNDEFINED, "123456", "password");
        UserDTO userDTO2 = new UserDTO(null, "userDTO2", "DTO", "testno@exist.co", "222-2222-2222", null, GenderList.UNDEFINED, "654321", "password");
        Roles defaultRole = new Roles(1L, RolesList.USER_ROLE);

        when(cacheSer.getFromRegistrationCache(Mockito.any())).thenAnswer(invocation -> {
            String email = invocation.getArgument(0);
            switch (email) {
                case "testno@exist.co":
                    return "123456";
                default:
                    return "000000";
            }
        });

        when(rolesRep.findByRole(RolesList.USER_ROLE)).thenReturn(Optional.of(defaultRole));

        assertThrows(RuntimeException.class, () -> userSer.createUser(userDTO1));
        assertThrows(RuntimeException.class, () -> userSer.createUser(userDTO2));
        UserDTO userDTO3 = new UserDTO(null, "userDTO2", "DTO", "testno@exist.co", "222-2222-2222", null, GenderList.UNDEFINED, "123456", "password");
        assertDoesNotThrow(() -> userSer.createUser(userDTO3));

        verify(userRep, times(1)).save(any());

        assertEquals("User Created", userSer.createUser(userDTO3));
    }
}
