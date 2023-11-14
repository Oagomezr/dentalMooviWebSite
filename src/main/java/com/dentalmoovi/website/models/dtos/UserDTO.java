package com.dentalmoovi.website.models.dtos;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.dentalmoovi.website.models.enums.GenderList;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {
    private Long idUser;
    private String firstName;
    private String lastName;
    private String email;
    private String celPhone;
    private LocalDate birthdate;
    private GenderList gender;
    private Set<RoleDTO> roles = new HashSet<>();
}
