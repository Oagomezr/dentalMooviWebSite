package com.dentalmoovi.website.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.dentalmoovi.website.models.dtos.CategoriesDTO;
import com.dentalmoovi.website.models.dtos.MessageDTO;
import com.dentalmoovi.website.models.entities.Categories;
import com.dentalmoovi.website.models.responses.CategoriesResponse;
import com.dentalmoovi.website.repositories.CategoriesRep;

@Service
public class CategoriesSer {
    private final CategoriesRep categoriesRep;

    public CategoriesSer(CategoriesRep categoriesRep){
        this.categoriesRep = categoriesRep;
    }

    private String categoryNotFound = "Category not found";

    @Cacheable(cacheNames = "getAllCategories")
    public CategoriesResponse getAllCategories(){
        List<Categories> parentCategories = categoriesRep.findParentCategories();
        List<CategoriesDTO> parentCategoriesDTO = new ArrayList<>();
        parentCategories.stream().forEach(parentCategory -> {
            List<String> itself = List.of(parentCategory.name());
            List<CategoriesDTO> subCategories = getSubCategories(parentCategory, itself);
            CategoriesDTO parentCategoryDTO = setParentCategoryDTO(List.of(parentCategory.name()), subCategories);
            parentCategoriesDTO.add(parentCategoryDTO);
        });
        return setCategoriesResponse(parentCategoriesDTO);
    }

    @CacheEvict(cacheNames = {"getAllCategories", "productsByCategory"}, allEntries = true)
    public MessageDTO updateCategoryName(String categoryName, String newName){
        Categories category = categoriesRep.findByName(categoryName)
            .orElseThrow(() -> new RuntimeException(categoryNotFound));
        categoriesRep.save(new Categories(category.id(), newName, category.idParentCategory()));
        return new MessageDTO("Category updated");
    }

    @CacheEvict(cacheNames = {"getAllCategories", "productsByCategory"}, allEntries = true)
    public MessageDTO updateCategoryPosition(String categoryName, String newPosition){
        Categories category = categoriesRep.findByName(categoryName)
            .orElseThrow(() -> new RuntimeException(categoryNotFound));
        if (newPosition.equals("-")) {
            Categories categoryUpdated = new Categories(category.id(), category.name(), null);
            categoriesRep.save(categoryUpdated);
        }else{
            Categories newParentCategory = categoriesRep.findByName(newPosition)
                .orElseThrow(() -> new RuntimeException("Category not found"));
            Categories categoryUpdated = new Categories(category.id(), category.name(), newParentCategory.id());
            categoriesRep.save(categoryUpdated);
        }
        return new MessageDTO("Category updated");
    }

    @CacheEvict(cacheNames = {"getAllCategories", "productsByCategory"}, allEntries = true)
    public MessageDTO addCategory(String parentCategoryName, String newCategoryName){
        if (parentCategoryName.equals("-")) {
            categoriesRep.save(new Categories(null, newCategoryName, null));
        }else{
            Categories parentCategory = categoriesRep.findByName(parentCategoryName)
                .orElseThrow(() -> new RuntimeException(categoryNotFound));
            categoriesRep.save(new Categories(null, newCategoryName, parentCategory.id()));
        }
        return new MessageDTO("Category created");
    }

    @SuppressWarnings("null")
    @CacheEvict(cacheNames = {"getAllCategories", "productsByCategory"}, allEntries = true)
    public MessageDTO deleteCategory(String categoryName){
        Categories category = categoriesRep.findByName(categoryName)
            .orElseThrow(() -> new RuntimeException(categoryNotFound));
        categoriesRep.delete(category);
        return new MessageDTO("Category deleted");
    }

    private List<CategoriesDTO> getSubCategories(Categories parentCategory, List<String> parents) {
        List<Categories> subCategories = categoriesRep.findByParentCategory(parentCategory.id());
        List<CategoriesDTO> subCategoriesDTO = new ArrayList<>();
        if(subCategories.isEmpty()) return subCategoriesDTO;  

        subCategories.stream().forEach(subCategory ->{
            List<String> itselfAndParents = new ArrayList<>(parents);
            itselfAndParents.add(0, subCategory.name());
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
