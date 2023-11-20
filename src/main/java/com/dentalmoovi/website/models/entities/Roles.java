package com.dentalmoovi.website.models.entities;

import org.springframework.data.annotation.Id;

import com.dentalmoovi.website.models.enums.RolesList;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Roles {
    @Id
    private Long id;
    @EqualsAndHashCode.Include
    private RolesList role;
}
