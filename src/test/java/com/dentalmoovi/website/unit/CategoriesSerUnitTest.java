package com.dentalmoovi.website.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dentalmoovi.website.Utils;
import com.dentalmoovi.website.models.dtos.CategoriesDTO;
import com.dentalmoovi.website.models.entities.Categories;
import com.dentalmoovi.website.models.responses.CategoriesResponse;
import com.dentalmoovi.website.repositories.CategoriesRep;
import com.dentalmoovi.website.services.CategoriesSer;


@ExtendWith(MockitoExtension.class)
class CategoriesSerUnitTest {
    
    @InjectMocks
    private CategoriesSer categoriesSer;

    @Mock
    private CategoriesRep categoriesRep;

    @Test
    void getAllCategoriesTest(){
        Categories parentCategoryX = Utils.setCategory("parent1", null);
        Categories parentCategoryY = Utils.setCategory("parent2", null);
        Categories subCategoryX1 = Utils.setCategory("subCategoryX1", parentCategoryX);
        Categories subCategoryX2 = Utils.setCategory("subCategoryX2", parentCategoryX);
        Categories subCategoryY1 = Utils.setCategory("subCategoryY1", parentCategoryY);
        Categories subSubCategoryX2 = Utils.setCategory("subSubCategoryX2", subCategoryX2);

        when(categoriesRep.findByParentCategoryIsNullOrderByName()).thenReturn(List.of(parentCategoryX, parentCategoryY));
        when(categoriesRep.findByParentCategoryOrderByName(Mockito.any())).thenAnswer(invocation -> {
            Categories parentCategory = invocation.getArgument(0);
            switch (parentCategory.getName()) {
                case "parent1":
                    return List.of(subCategoryX1, subCategoryX2);
                case "parent2":
                    return List.of(subCategoryY1);
                case "subCategoryX2":
                    return List.of(subSubCategoryX2);
                default:
                    return List.of();
            }
        });

        CategoriesResponse response = categoriesSer.getAllCategories();
        CategoriesDTO result1 = response.getData().get(0).getChildrenCategories().get(1);
        List<String> expected1 = List.of("subCategoryX2","parent1");
        CategoriesDTO result2 = result1.getChildrenCategories().get(0);
        List<String> expected2 = List.of("subSubCategoryX2","subCategoryX2","parent1");
        //assertEquals(expected1, response.getData().get(0).getCategoryAndParents());
        assertEquals(expected1, result1.getCategoryAndParents());
        assertEquals(expected2, result2.getCategoryAndParents());
    }
}
