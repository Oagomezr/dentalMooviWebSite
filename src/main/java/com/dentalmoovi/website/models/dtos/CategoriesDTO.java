package com.dentalmoovi.website.models.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriesDTO {

    private List<String> categoryAndParents;
    private List<CategoriesDTO> childrenCategories;
    
}
