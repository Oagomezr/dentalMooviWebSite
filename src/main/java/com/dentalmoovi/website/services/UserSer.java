package com.dentalmoovi.website.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.mail.MailException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dentalmoovi.website.Utils;
import com.dentalmoovi.website.models.dtos.AddressesDTO;
import com.dentalmoovi.website.models.dtos.MessageDTO;
import com.dentalmoovi.website.models.dtos.UserDTO;
import com.dentalmoovi.website.models.entities.Addresses;
import com.dentalmoovi.website.models.entities.Roles;
import com.dentalmoovi.website.models.entities.Users;
import com.dentalmoovi.website.models.entities.enums.Departaments;
import com.dentalmoovi.website.models.entities.enums.MunicipalyCity;
import com.dentalmoovi.website.models.entities.enums.RolesList;
import com.dentalmoovi.website.models.exceptions.AlreadyExistException;
import com.dentalmoovi.website.models.exceptions.IncorrectException;
import com.dentalmoovi.website.models.responses.AddressesResponse;
import com.dentalmoovi.website.repositories.AddressesRep;
import com.dentalmoovi.website.repositories.RolesRep;
import com.dentalmoovi.website.repositories.UserRep;
import com.dentalmoovi.website.repositories.enums.DepartamentsRep;
import com.dentalmoovi.website.repositories.enums.MunicipalyRep;
import com.dentalmoovi.website.security.LoginDTO;
import com.dentalmoovi.website.security.MainUser;
import com.dentalmoovi.website.security.PwDTO;
import com.dentalmoovi.website.services.cache.CacheSer;

@Service
public class UserSer {

    Random random = new Random();
    private final CacheSer cacheSer;
    private final EmailSer emailSer;
    private final UserRep userRep;
    private final RolesRep rolesRep;
    private final AddressesRep addressesRep;
    private final MunicipalyRep municipalyRep;
    private final DepartamentsRep departamentsRep;

    private static final BCryptPasswordEncoder pwe = new BCryptPasswordEncoder();

    @Value("${spring.mail.otherPassword}")
    private String ref; 

    public UserSer(CacheSer cacheSer, EmailSer emailSer, UserRep userRep, RolesRep rolesRep, AddressesRep addressesRep,
            MunicipalyRep municipalyRep, DepartamentsRep departamentsRep) {
        this.cacheSer = cacheSer;
        this.emailSer = emailSer;
        this.userRep = userRep;
        this.rolesRep = rolesRep;
        this.addressesRep = addressesRep;
        this.municipalyRep = municipalyRep;
        this.departamentsRep = departamentsRep;
    }

    public void sendEmailNotification(String email, String subject, String body) {
        String retrictReplay = cacheSer.getFromReplayCodeRestrict(email);
        if (retrictReplay !=null && retrictReplay.equals(email)) return;

        //Generate randomNumber
        int randomNumber = random.nextInt(1000000);

        //generate a format to add zeros to the left in case randomNumber < 100000
        String formattedNumber = String.format("%06d", randomNumber);

        body += formattedNumber;
        
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
                if(checkEmailExists(userDTO.email(), true)) 
                    throw new AlreadyExistException("That user already exist");

                //get verification code
                String code = cacheSer.getFromRegistrationCache(userDTO.email());

                //verify if code sended is equals the verification code
                if(!userDTO.code().equals(code)) 
                    throw new IncorrectException("The code: "+code+" is incorrect");

                //create default role
                Roles defaultRole = rolesRep.findByRole(RolesList.USER_ROLE)
                    .orElseThrow(() -> new RuntimeException("Role not found"));

                //encrypt the password
                String hashedPassword = pwe.encode(userDTO.password()); 

                //set and save user
                Users newUser = 
                    new Users(
                        null, userDTO.firstName(), userDTO.lastName(), userDTO.email(), userDTO.celPhone(), 
                        userDTO.birthdate(), userDTO.gender(), hashedPassword, null, null);
                newUser.addRole(defaultRole);
                
                userRep.save(newUser);

                return "User Created";
            }
        }

        CreateUser innerClass = new CreateUser();
        return innerClass.createUser();
    }

    //verify if user exist
    public boolean checkEmailExists(String value, boolean signup) {
        boolean result = userRep.existsByEmailIgnoreCase(value);
        return signup ? result : !result;
    }

    @Cacheable("getName")
    public MessageDTO getName(String cacheRef){
        return new MessageDTO(getUserAuthenticated(cacheRef).firstName());
    }
    
    @Cacheable("getUserAuthenticated")
    public Users getUserAuthenticated(String cacheRef){
        String userName = getUsername(cacheRef);
        return Utils.getUserByEmail(userName, userRep);
    }

    @Cacheable("getUserAuthDTO")
    public UserDTO getUserAuthDTO(String cacheRef){
        Users user = getUserAuthenticated(cacheRef);
        return new UserDTO(null, user.firstName(), user.lastName(), user.email(), user.celPhone(), user.birthdate(), user.gender(), null, null);
    }

    public MainUser getMainUser(String email){
        Users user = Utils.getUserByEmail(email, userRep);
        return MainUser.build(user, rolesRep, ref);
    }

    @Cacheable("getUserDetails")
    public String getUserDetails(String email){
        MainUser mainUser = getMainUser(email);
        boolean isAdmin = mainUser.getAuthorities().stream()
            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority()
                .equals(RolesList.ADMIN_ROLE.name()));
        if (isAdmin) return "A" + mainUser.getCacheRef();

        return mainUser.getCacheRef();
    }

    @CacheEvict(value = {"getName", "getUserAuthenticated", "getUserAuthDTO"}, key = "#cacheRef")
    public MessageDTO updateUserInfo(UserDTO userDTO, String cacheRef){
        Users user = Utils.getUserByEmail(getUsername(cacheRef), userRep);
        
        userRep.save(
            new Users(
                user.id(), userDTO.firstName(), userDTO.lastName(), user.email(), 
                userDTO.celPhone(), userDTO.birthdate(), userDTO.gender(), 
                user.password(), user.roles(), user.addresses()));

        return new MessageDTO("User updated");
    }

    @Cacheable("getAddresses")
    public AddressesResponse getAddresses(String cacheRef){
        Users user = getUserAuthenticated(cacheRef);
        List<Long> idsAddresses = new ArrayList<>(user.getAddressesIds());
        List<AddressesDTO> addressesDTO = new ArrayList<>();
        
        idsAddresses.stream().forEach(id ->{

            Addresses address = addressesRep.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

            MunicipalyCity municipaly = municipalyRep.findById(address.idMunicipalyCity())
                .orElseThrow(() -> new RuntimeException("Municipaly not found"));

            Departaments departament = departamentsRep.findById(municipaly.id_departament())
                .orElseThrow(() -> new RuntimeException("Departament not found"));

            AddressesDTO addressDTO = new AddressesDTO(
                address.id(), address.address(), address.phone(), address.description(), 
                municipaly.name(), departament.name(), 0);
                
            addressesDTO.add(addressDTO);
        });

        return new AddressesResponse(addressesDTO);
    }

    @CacheEvict(value = {"getAddresses", "getUserByRep"},  key = "#cacheRef")
    public MessageDTO createAddress(AddressesDTO addressDTO, String cacheRef){
            
        Users user = getUserAuthenticated(cacheRef);

        Addresses address = new Addresses(
            null, addressDTO.address(), addressDTO.phone(), addressDTO.description(), addressDTO.idMunicipaly());

        address = addressesRep.save(address);
        
        user.addAddress(address);
        userRep.save(user);

        return new MessageDTO("Address created");
    }

    @CacheEvict(value = "getAddresses", key = "#cacheRef")
    public MessageDTO updateAddress(AddressesDTO addressDTO, String cacheRef){
        addressesRep.save(
            new Addresses(
                addressDTO.id(), addressDTO.address(), addressDTO.phone(), 
                addressDTO.description(), addressDTO.idMunicipaly())
        );
        return new MessageDTO("Address updated");
    }

    @CacheEvict(value = {"getAddresses", "getUserByRep"}, key = "#cacheRef")
    public MessageDTO deleteAddress(long id, String cacheRef){
        Users user = getUserAuthenticated(cacheRef);
        user.deleteAddress(id);
        userRep.save(user);
        addressesRep.deleteById(id);
        return new MessageDTO("Address deleted");
    }

    public MessageDTO changePw(PwDTO dto, String cacheRef){
        Users user = getUserAuthenticated(cacheRef);
        boolean valid = pwe.matches(dto.oldP(), user.password());

        if (valid) {
            return updatePw(user, dto.newP());
        }
        throw new IncorrectException("Incorrect Password");
    }

    public MessageDTO rememberPw(LoginDTO userCredentials){ 
        //get verification code
        String code = cacheSer.getFromRegistrationCache(userCredentials.userName());

        //verify if code sended is equals the verification code
        if(!userCredentials.code().equals(code)) 
            throw new IncorrectException("The code: "+code+" is incorrect");

        cacheSer.removeFromRegistrationCache(userCredentials.userName());

        String newPw = generateRandomString(10);

        String subject = "Recuperación de contraseña";
        String body = "Dental Moovi recibió una solicitud de recuperación de contraseña.\n\n"+
                        "Su nueva contraseña es: " + newPw + "\n\n"+
                        "Recuerde que puede cambiarla a travez de la pagina Web de Dental Moovi";

        Users user = Utils.getUserByEmail(userCredentials.userName(), userRep);

        try {
            emailSer.sendEmail(userCredentials.userName(), subject, body);
            return updatePw(user, newPw);
        } catch (MailException e) {
            // Manage the exception in case it cannot send the email
            e.printStackTrace();
            throw new MailException(e.getMessage()) {};
        }
    }

    @Cacheable("getUserName")
    private String getUsername(String cacheRef){
        Authentication authentication = 
            SecurityContextHolder.getContext().getAuthentication();

        // Verify if the user is authenticated
        if (authentication != null && authentication.isAuthenticated()) 
            return authentication.getName(); //Get username
        
        throw new NoSuchElementException("The user is not authenticated");
    }

    private MessageDTO updatePw(Users user, String pw){
        String pwNew = pwe.encode(pw);
        userRep.save(new Users(
            user.id(), user.firstName(), user.lastName(), user.email(), user.celPhone(), 
            user.birthdate(), user.gender(), pwNew, user.roles(), user.addresses()));
        return new MessageDTO("Password updated successfully");
    }

    private String generateRandomString(int length){
        // Characters that will use to generate random string
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~%$#";
        StringBuilder stringBuilder = new StringBuilder();

        // Generate random string
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }
}
