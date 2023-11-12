package com.dentalmoovi.website.models.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.dentalmoovi.website.models.enums.GenderList;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 25)
    private String firstName;

    @Column(nullable = false, length = 25)
    private String lastName;

    @Column(nullable = false, unique = true, length = 60)
    @EqualsAndHashCode.Include
    private String email;

    @Column(nullable = false, length = 12)
    private String celPhone;

    @Column(nullable = true)
    private LocalDate birthdate;

    @Column(nullable = false, length = 25)
    @Enumerated(EnumType.STRING)
    private GenderList gender;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "users_roles",
                joinColumns = { @JoinColumn(name = "id_user") },
                inverseJoinColumns = { @JoinColumn(name = "id_role") })
    private Set<Roles> roles = new HashSet<>();
}
