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
        Categories parentCategoryX = new Categories(1L, "parent1", null);
        
        Categories parentCategoryY = new Categories(2L, "parent2", null);
        
        Categories subCategoryX1 = new Categories(3L, "subCategoryX1", parentCategoryX.id());
        
        Categories subCategoryX2 = new Categories(4L, "subCategoryX2", parentCategoryX.id());
        
        Categories subCategoryY1 = new Categories(5L, "subCategoryY1", parentCategoryY.id());
        
        Categories subSubCategoryX2 = new Categories(6L, "subSubCategoryX2", subCategoryX2.id());
        

        when(categoriesRep.findParentCategories()).thenReturn(List.of(parentCategoryX, parentCategoryY));
        when(categoriesRep.findByParentCategory(Mockito.any())).thenAnswer(invocation -> {
            Long idCategory = invocation.getArgument(0);
            int intIdCategory = idCategory.intValue();
            switch (intIdCategory) {
                case 1:
                    return List.of(subCategoryX1, subCategoryX2);
                case 2:
                    return List.of(subCategoryY1);
                case 4:
                    return List.of(subSubCategoryX2);
                default:
                    return List.of();
            }
        });

        CategoriesResponse response = categoriesSer.getAllCategories();
        CategoriesDTO result1 = response.getData().get(0).childrenCategories().get(1);
        List<String> expected1 = List.of("subCategoryX2","parent1");
        CategoriesDTO result2 = result1.childrenCategories().get(0);
        List<String> expected2 = List.of("subSubCategoryX2","subCategoryX2","parent1");
        //assertEquals(expected1, response.getData().get(0).getCategoryAndParents());
        assertEquals(expected1, result1.categoryAndParents());
        assertEquals(expected2, result2.categoryAndParents());
    }
}
