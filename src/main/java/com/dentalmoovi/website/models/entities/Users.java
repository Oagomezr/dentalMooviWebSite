package com.dentalmoovi.website.models.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;

import com.dentalmoovi.website.models.entities.enums.GenderList;
import com.dentalmoovi.website.models.entities.many_to_many.UsersRoles;
import com.dentalmoovi.website.models.entities.many_to_many.UsersAddresses;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Users {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    @EqualsAndHashCode.Include
    private String email;
    private String celPhone;
    private LocalDate birthdate;
    private GenderList gender;
    private String password;

    @MappedCollection(idColumn = "id_user")
    private Set<UsersRoles> roles = new HashSet<>();

    @MappedCollection(idColumn = "id_user")
    private Set<UsersAddresses> addresses = new HashSet<>();

    public void addRole(Roles role){
        this.roles.add(new UsersRoles(id, role.getId()));
    }

    public Set<Long> getRolesIds(){
        return this.roles.stream()
                    .map(UsersRoles::idRole)
                    .collect(Collectors.toSet());
    }

    public void addAddress(Addresses address){
        this.addresses.add(new UsersAddresses(id,address.id()));
    }

    public Set<Long> getAddressesIds(){
        return this.addresses.stream()
                    .map(UsersAddresses::idAddress)
                    .collect(Collectors.toSet());
    }

    public void deleteAddress(Long addressId) {
        this.addresses.removeIf(addressRef -> addressRef.idAddress().equals(addressId));
    }
}
