package com.dentalmoovi.website.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dentalmoovi.website.Utils;
import com.dentalmoovi.website.models.dtos.AddressesDTO;
import com.dentalmoovi.website.models.dtos.MessageDTO;
import com.dentalmoovi.website.models.dtos.UserDTO;
import com.dentalmoovi.website.models.entities.Addresses;
import com.dentalmoovi.website.models.entities.Roles;
import com.dentalmoovi.website.models.entities.Users;
import com.dentalmoovi.website.models.enums.RolesList;
import com.dentalmoovi.website.models.responses.AddressesResponse;
import com.dentalmoovi.website.repositories.AddressesRep;
import com.dentalmoovi.website.repositories.RolesRep;
import com.dentalmoovi.website.repositories.UserRep;
import com.dentalmoovi.website.security.MainUser;
import com.dentalmoovi.website.services.cache.CacheSer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSer {

    Random random = new Random();
    private final CacheSer cacheSer;
    private final EmailSer emailSer;
    private final UserRep userRep;
    private final RolesRep rolesRep;
    private final AddressesRep addressesRep;
    private static Logger logger = LoggerFactory.getLogger(UserSer.class);

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
                if(checkEmailExists(userDTO.getEmail())) 
                    throw new RuntimeException("That user already exist");

                //get verification code
                String code = cacheSer.getFromRegistrationCache(userDTO.getEmail());

                //verify if code sended is equals the verification code
                if(!userDTO.getCode().equals(code)) 
                    throw new RuntimeException("That code is incorrect");

                //create default role
                Roles defaultRole = rolesRep.findByRole(RolesList.USER_ROLE)
                    .orElseThrow(() -> new RuntimeException("Role not found"));

                //encrypt the password
                String hashedPassword = new BCryptPasswordEncoder().encode(userDTO.getPassword()); 

                //set and save user
                Utils.setUser(userDTO.getFirstName(), userDTO.getLastName(), 
                    userDTO.getEmail(), userDTO.getCelPhone(), userDTO.getGender(), hashedPassword, userDTO.getBirthdate(), defaultRole, userRep);

                return "User Created";
            }
        }

        CreateUser innerClass = new CreateUser();
        return innerClass.createUser();
    }

    //verify if user exist
    public boolean checkEmailExists(String value) {
        return userRep.existsByEmail(value);
    }

    @Cacheable("getUserAuthenticatedCache")
    public UserDTO getUserAuthenticated(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String userName = userDetails.getUsername();
        Users user= this.userRep.findByEmail(userName)
            .orElseThrow(() -> new RuntimeException("User not found"));

        return Utils.setUserDTO(user.getFirstName(), user.getLastName(), user.getEmail(), user.getCelPhone(), user.getGender(), user.getBirthdate(), null, null);
    }

    public MainUser getMainUser(String email){
        Users user = userRep.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return MainUser.build(user, rolesRep);
    }

    public boolean isAdmin(String email){
        MainUser mainUser = getMainUser(email);
        return mainUser.getAuthorities().stream()
            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority()
                .equals(RolesList.ADMIN_ROLE.name()));
    }

    @CacheEvict(value = "getUserAuthenticatedCache", allEntries = true)
    public MessageDTO updateUserInfo(UserDTO userDTO){
        Users user = userRep.findByEmail(getUserAuthenticated().getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setBirthdate(userDTO.getBirthdate());
        user.setGender(userDTO.getGender());
        user.setCelPhone(userDTO.getCelPhone());
        userRep.save(user);
        return new MessageDTO("User updated");
    }

    @CacheEvict(value = "getAddresses", allEntries = true)
    public MessageDTO createAddress(AddressesDTO addressDTO){
            
        Users user = userRep.findByEmail(getUserAuthenticated().getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

        
        Addresses address = Utils.setAddress(addressDTO.getDepartament(), 
            addressDTO.getLocation(), addressDTO.getAddress(), addressDTO.getPhone(), 
            addressDTO.getDescription(), addressesRep);
        
        user.addAddress(address);
        userRep.save(user);

        return new MessageDTO("Address created");
            
    }

    @CacheEvict(value = "getAddresses", allEntries = true)
    public MessageDTO updateAddress(AddressesDTO addressDTO){
        Addresses address = addressesRep.findById(addressDTO.getId())
            .orElseThrow(() -> new RuntimeException("Address not found"));
        logger.info(address.getDescription());
        address.setDepartament(addressDTO.getDepartament());
        address.setLocation(addressDTO.getLocation());
        address.setAddress(addressDTO.getAddress());
        address.setPhone(addressDTO.getPhone());
        address.setDescription(addressDTO.getDescription());
        addressesRep.save(address);
        return new MessageDTO("Address updated");
    }

    @Cacheable("getAddresses")
    public AddressesResponse getAddresses(){
        Users user = userRep.findByEmail(getUserAuthenticated().getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));
        List<Long> idsAddresses = new ArrayList<>(user.getAddressesIds());
        List<AddressesDTO> addressesDTO = new ArrayList<>();
        idsAddresses.stream().forEach(id ->{
            Addresses address = addressesRep.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));
            AddressesDTO addressDTO = Utils.setAddressDTO(id, address.getDepartament(), address.getLocation(), 
                address.getAddress(), address.getPhone(), address.getDescription());
            addressesDTO.add(addressDTO);
        });
        AddressesResponse response = new AddressesResponse();
        response.setData(addressesDTO);
        return response;
    }

    @CacheEvict(value = "getAddresses", allEntries = true)
    public MessageDTO deleteAddress(long id){
        Users user = userRep.findByEmail(getUserAuthenticated().getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.deleteAddress(id);
        userRep.save(user);
        addressesRep.deleteById(id);
        return new MessageDTO("Address deleted");
    }
}
