package com.dentalmoovi.website.models.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;

import com.dentalmoovi.website.models.enums.GenderList;

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
    private Set<RolesRef> roles = new HashSet<>();

    @MappedCollection(idColumn = "id_user")
    private Set<AddressesRef> addresses = new HashSet<>();

    public void addRole(Roles role){
        this.roles.add(new RolesRef(role.getId()));
    }

    public Set<Long> getRolesIds(){
        return this.roles.stream()
                    .map(RolesRef::getIdRole)
                    .collect(Collectors.toSet());
    }

    public void addAddress(Addresses address){
        this.addresses.add(new AddressesRef(address.getId()));
    }

    public Set<Long> getAddressesIds(){
        return this.addresses.stream()
                    .map(AddressesRef::getIdAddress)
                    .collect(Collectors.toSet());
    }

    public void deleteAddress(Long addressId) {
        this.addresses.removeIf(addressRef -> addressRef.getIdAddress().equals(addressId));
    }
}
