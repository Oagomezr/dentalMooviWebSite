package com.dentalmoovi.website.models.entities;

import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;

@Table("users_roles")
@Data
@AllArgsConstructor
public class RolesRef {
    private Long idRole;
}
