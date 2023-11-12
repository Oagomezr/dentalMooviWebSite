package com.dentalmoovi.website.models.entities;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Categories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_category")
    private Long idCategory;

    @EqualsAndHashCode.Include
    @Column(nullable = false, length = 70, unique = true)
    private String name;

    @Column(length = 50)
    private int numberUpdates;

    @ManyToOne
    @JoinColumn(name = "parent_category")
    private Categories parentCategory;

    @OneToMany(mappedBy = "category", targetEntity = Products.class)
    private Set<Products> products;
}
