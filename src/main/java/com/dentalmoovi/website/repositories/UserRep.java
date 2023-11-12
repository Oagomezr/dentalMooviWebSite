package com.dentalmoovi.website.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.dentalmoovi.website.models.entities.Users;

public interface UserRep extends JpaRepository<Users,Long>{
    Boolean existsByEmail(@Param("email") String email);
    Optional<Users> findByEmail(@Param("email") String email);
}
