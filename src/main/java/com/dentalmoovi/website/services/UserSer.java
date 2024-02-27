package com.dentalmoovi.website.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/* import org.slf4j.Logger;
import org.slf4j.LoggerFactory; */
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dentalmoovi.website.models.dtos.AddressesDTO;
import com.dentalmoovi.website.models.dtos.MessageDTO;
import com.dentalmoovi.website.models.dtos.UserDTO;
import com.dentalmoovi.website.models.entities.Addresses;
import com.dentalmoovi.website.models.entities.Roles;
import com.dentalmoovi.website.models.entities.Users;
import com.dentalmoovi.website.models.entities.enums.Departaments;
import com.dentalmoovi.website.models.entities.enums.MunicipalyCity;
import com.dentalmoovi.website.models.entities.enums.RolesList;
import com.dentalmoovi.website.models.responses.AddressesResponse;
import com.dentalmoovi.website.repositories.AddressesRep;
import com.dentalmoovi.website.repositories.RolesRep;
import com.dentalmoovi.website.repositories.UserRep;
import com.dentalmoovi.website.repositories.enums.DepartamentsRep;
import com.dentalmoovi.website.repositories.enums.MunicipalyRep;
import com.dentalmoovi.website.security.MainUser;
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
    //private static Logger logger = LoggerFactory.getLogger(UserSer.class);

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
                if(checkEmailExists(userDTO.email())) 
                    throw new RuntimeException("That user already exist");

                //get verification code
                String code = cacheSer.getFromRegistrationCache(userDTO.email());

                //verify if code sended is equals the verification code
                if(!userDTO.code().equals(code)) 
                    throw new RuntimeException("That code is incorrect");

                //create default role
                Roles defaultRole = rolesRep.findByRole(RolesList.USER_ROLE)
                    .orElseThrow(() -> new RuntimeException("Role not found"));

                //encrypt the password
                String hashedPassword = new BCryptPasswordEncoder().encode(userDTO.password()); 

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
    @Cacheable("checkEmail")
    public boolean checkEmailExists(String value) {
        return userRep.existsByEmail(value);
    }

    @Cacheable("getName")
    public MessageDTO getName(String cacheRef){
        return new MessageDTO(getUserAuthenticated(cacheRef).firstName());
    }
    
    @Cacheable("getUserAuthenticated")
    public UserDTO getUserAuthenticated(String cacheRef){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String userName = userDetails.getUsername();
        Users user= userRep.findByEmail(userName)
            .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserDTO(null, user.firstName(), user.lastName(), user.email(), user.celPhone(), user.birthdate(), user.gender(), null, null);
    }

    public MainUser getMainUser(String email){
        Users user = userRep.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

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

    @CacheEvict(value = {"getName", "getUserAuthenticated"}, key = "#cacheRef")
    public MessageDTO updateUserInfo(UserDTO userDTO, String cacheRef){
        Users user = userRep.findByEmail(getUserAuthenticated(cacheRef).email())
            .orElseThrow(() -> new RuntimeException("User not found"));
        userRep.save(new Users(user.id(), userDTO.firstName(), userDTO.lastName(), user.email(), userDTO.celPhone(), userDTO.birthdate(), userDTO.gender(), user.password(), null, null));
        return new MessageDTO("User updated");
    }

    @SuppressWarnings("null")
    @Cacheable("getAddresses")
    public AddressesResponse getAddresses(String cacheRef){
        Users user = getUserFromRep(cacheRef);
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
            
        Users user = getUserFromRep(cacheRef);

        Addresses address = new Addresses(
            null, addressDTO.address(), addressDTO.phone(), addressDTO.description(), addressDTO.idMunicipaly());

        addressesRep.save(address);
        
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
        Users user = getUserFromRep(cacheRef);
        user.deleteAddress(id);
        userRep.save(user);
        addressesRep.deleteById(id);
        return new MessageDTO("Address deleted");
    }

    @Cacheable("getUserByRep")
    private Users getUserFromRep(String cacheRef){
        return userRep.findByEmail(getUserAuthenticated(cacheRef).email())
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
