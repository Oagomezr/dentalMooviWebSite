package com.dentalmoovi.website.models.entities;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Categories {
    @Id
    private Long id;
    @EqualsAndHashCode.Include
    private String name;
    private Long idParentCategory;
}
