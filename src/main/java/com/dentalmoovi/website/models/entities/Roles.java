package com.dentalmoovi.website.models.entities;

import java.util.HashSet;
import java.util.Set;

import com.dentalmoovi.website.models.enums.RolesList;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 25)
    @Enumerated(EnumType.STRING)
    @EqualsAndHashCode.Include
    private RolesList role;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private Set<Users> users = new HashSet<>();
}
