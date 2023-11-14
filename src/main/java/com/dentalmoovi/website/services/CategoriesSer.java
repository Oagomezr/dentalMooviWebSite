package com.dentalmoovi.website.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dentalmoovi.website.models.dtos.CategoriesDTO;
import com.dentalmoovi.website.models.entities.Categories;
import com.dentalmoovi.website.models.responses.CategoriesResponse;
import com.dentalmoovi.website.repositories.CategoriesRep;

@Service
public class CategoriesSer {
    private final CategoriesRep categoriesRep;

    public CategoriesSer(CategoriesRep categoriesRep){
        this.categoriesRep = categoriesRep;
    }

    public CategoriesResponse getAllCategories(){
        List<Categories> parentCategories = categoriesRep.findByParentCategoryIsNullOrderByName();
        List<CategoriesDTO> parentCategoriesDTO = new ArrayList<>();
        parentCategories.stream().forEach(parentCategory -> {
            List<String> itself = List.of(parentCategory.getName());
            List<CategoriesDTO> subCategories = getSubCategories(parentCategory, itself);
            CategoriesDTO parentCategoryDTO = setParentCategoryDTO(List.of(parentCategory.getName()), subCategories);
            parentCategoriesDTO.add(parentCategoryDTO);
        });
        return setCategoriesResponse(parentCategoriesDTO);
    }

    public String checkUpdate(){
        return String.valueOf(categoriesRep.findMaxId() + categoriesRep.countUpdates());
    }

    private List<CategoriesDTO> getSubCategories(Categories parentCategory, List<String> parents) {
        List<Categories> subCategories = categoriesRep.findByParentCategoryOrderByName(parentCategory);
        List<CategoriesDTO> subCategoriesDTO = new ArrayList<>();
        if(subCategories.isEmpty()) return subCategoriesDTO;  

        subCategories.stream().forEach(subCategory ->{
            List<String> itselfAndParents = new ArrayList<>(parents);
            itselfAndParents.add(0, subCategory.getName());
            List<CategoriesDTO> subcategoriesOfSubcategory = getSubCategories(subCategory, itselfAndParents);
            CategoriesDTO subCategoryDTO = setParentCategoryDTO(itselfAndParents, subcategoriesOfSubcategory);
            subCategoriesDTO.add(subCategoryDTO);
        });
        return subCategoriesDTO;
    }

    private CategoriesDTO setParentCategoryDTO(List<String> itselfAndParents, List<CategoriesDTO> childrenCategories){
        CategoriesDTO categoryDTO = new CategoriesDTO();
        categoryDTO.setCategoryAndParents(itselfAndParents);
        categoryDTO.setChildrenCategories(childrenCategories);
        return categoryDTO;
    }

    private CategoriesResponse setCategoriesResponse(List<CategoriesDTO> data){
        CategoriesResponse categoriesResponse = new CategoriesResponse();
        categoriesResponse.setData(data);
        return categoriesResponse;
    }
}
