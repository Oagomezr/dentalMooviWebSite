package com.dentalmoovi.website.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.dentalmoovi.website.models.entities.Roles;
import com.dentalmoovi.website.models.enums.RolesList;

public interface RolesRep extends JpaRepository<Roles,Long>{
    public Optional<Roles> findByRole(@Param("name_role") RolesList nameRole);
}
